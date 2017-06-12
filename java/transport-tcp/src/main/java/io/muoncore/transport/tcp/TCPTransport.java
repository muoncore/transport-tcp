package io.muoncore.transport.tcp;

import io.muoncore.Discovery;
import io.muoncore.channel.ChannelConnection;
import io.muoncore.channel.Channels;
import io.muoncore.channel.support.Scheduler;
import io.muoncore.codec.Codecs;
import io.muoncore.codec.json.JsonOnlyCodecs;
import io.muoncore.exception.MuonTransportFailureException;
import io.muoncore.exception.NoSuchServiceException;
import io.muoncore.message.MuonInboundMessage;
import io.muoncore.message.MuonOutboundMessage;
import io.muoncore.protocol.ServerStacks;
import io.muoncore.transport.MuonTransport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.*;
import java.net.*;



@Slf4j
public class TCPTransport implements MuonTransport {

    static int PORT = 4596;

    private ServerSocket serverSocket;

    @Override
    public void shutdown() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Discovery discovery, ServerStacks serverStacks, Codecs codecs, Scheduler scheduler) throws MuonTransportFailureException {

        try {
            serverSocket = new ServerSocket(PORT);

            new Thread(() -> {
                try {
                    while(true) {
                        TCPChannel tcpChannel = TCPChannel.negotiateServer(serverSocket.accept());
                        log.warn("Received incoming request. generating a server channel");
                        ChannelConnection<MuonInboundMessage, MuonOutboundMessage> stackChannel = serverStacks.openServerChannel(tcpChannel.getProtocol());
                        Channels.connect(tcpChannel, stackChannel);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrlScheme() {
        return "tcp";
    }

    @Override
    public URI getLocalConnectionURI() {
        try {
            return new URI("tcp://localhost:" + PORT);
        } catch (URISyntaxException e) {
            throw new MuonTransportFailureException("Unable to generate a conneciton uri", e);
        }
    }

    @Override
    public boolean canConnectToService(String s) {
        return true;
    }

    @Override
    public ChannelConnection<MuonOutboundMessage, MuonInboundMessage> openClientChannel(String s, String s1) throws NoSuchServiceException, MuonTransportFailureException {
        try {
            return TCPChannel.negotiateClient(s, s1);
        } catch (IOException e) {
            throw new MuonTransportFailureException("Unable to open a TCP Channel", e);
        }
    }

    @Getter
    @Slf4j
    public static class TCPChannel implements ChannelConnection<MuonOutboundMessage, MuonInboundMessage> {

        private Socket socket;
        private String protocol;
        private ChannelFunction<MuonInboundMessage> channelFunction;

        private Codecs codecs = new JsonOnlyCodecs();

        private PrintWriter out;

        public TCPChannel(Socket socket, String protocol) throws IOException {
            this.socket = socket;
            this.protocol = protocol;

            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {

                    InputStream in = socket.getInputStream();
                    DataInputStream dis = new DataInputStream(in);

                    while (true) {
                        int len = dis.readInt();
                        byte[] data = new byte[len];
                        dis.readFully(data);

                        log.info("Read all data for the message");

                        MuonInboundMessage msg = codecs.decode(data, "application/json", MuonInboundMessage.class);

                        channelFunction.apply(msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        public static TCPChannel negotiateClient(String server, String protocol) throws IOException {
            //TODO, lookup the connection info for the server

            Socket socket = new Socket("localhost", PORT);

            PrintWriter out =
                    new PrintWriter(socket.getOutputStream(), true);

            out.println(protocol);

            log.info("Sent Client protocol negotiation, switching to binary message comms");

            return new TCPChannel(socket, protocol);
        }

        public static TCPChannel negotiateServer(Socket socket) throws IOException {

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            String proto = in.readLine();

            log.info("Server protocol negotiated as {}, switching to binary message comms", proto);

            return new TCPChannel(socket, proto);
        }

        @Override
        public void receive(ChannelFunction<MuonInboundMessage> channelFunction) {
            this.channelFunction = channelFunction;
        }

        @Override
        public void send(MuonOutboundMessage muonOutboundMessage) {
            try {

                Codecs.EncodingResult encode = codecs.encode(muonOutboundMessage, codecs.getAvailableCodecs());

                byte[] payload = encode.getPayload();

                OutputStream out = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);

                dos.writeInt(payload.length);
                dos.write(payload);

                dos.flush();
                log.info("Wrote data for outgoing message");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void shutdown() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
