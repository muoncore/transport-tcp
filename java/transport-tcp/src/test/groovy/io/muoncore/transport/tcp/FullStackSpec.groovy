package io.muoncore.transport.tcp

import io.muoncore.MultiTransportMuon
import io.muoncore.Muon
import io.muoncore.channel.ChannelConnection
import io.muoncore.channel.impl.StandardAsyncChannel
import io.muoncore.codec.json.GsonCodec
import io.muoncore.codec.json.JsonOnlyCodecs
import io.muoncore.config.AutoConfiguration
import io.muoncore.descriptors.ProtocolDescriptor
import io.muoncore.memory.discovery.InMemDiscovery
import io.muoncore.message.MuonInboundMessage
import io.muoncore.message.MuonMessageBuilder
import io.muoncore.protocol.ServerProtocolStack
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class FullStackSpec extends Specification {
    @Shared
    def discovery = new InMemDiscovery()

//    @AutoCleanup("shutdown")
//    @Shared
//    Muon muon1

    @AutoCleanup("shutdown")
    @Shared
    Muon muon2

    def setupSpec() {
        def simples
//        muon1 = muon("clientservice")
        muon2 = muon("targetservice")
    }

    def "can connect between services and exchange messages"() {
        def sendToClient

        ChannelConnection connection = Mock(ChannelConnection) {
            receive(_) >> {
                sendToClient = new ChannelFunctionExecShimBecauseGroovyCantCallLambda(it[0])
                Thread.start {
                    sleep(500)
                    sendToClient(outbound("awesome", "rpc"))
                }
            }
        }
        def protocol = Mock(ServerProtocolStack) {
            createChannel() >> connection
            getProtocolDescriptor() >> new ProtocolDescriptor("rpc", "rpc", "hello", [])
        }

        muon2.protocolStacks.registerServerProtocol(protocol)

        MuonInboundMessage msg

        StandardAsyncChannel.echoOut = true

        def channel = muon2.transportClient.openClientChannel()

        channel.receive {
            msg = it
        }

        when: "Send a message to open the channel"
        channel.send(outbound("targetservice", "rpc"))

        then:

        new PollingConditions(timeout: 3).eventually {
            msg != null
            msg.step == "somethingHappened"
        }

        cleanup:
        StandardAsyncChannel.echoOut = false
    }

    def outbound(String service, String protocol) {
        MuonMessageBuilder
                .fromService("localService")
                .toService(service)
                .step("somethingHappened")
                .protocol(protocol)
                .contentType("application/json")
                .payload(new GsonCodec().encode([:]))
                .build()
    }

    private def muon(serviceName) {

        def config = new AutoConfiguration(serviceName: serviceName)

        TCPTransport transport = new TCPTransport()

        def muon = new MultiTransportMuon(config, discovery, [transport], new JsonOnlyCodecs())

        return muon
    }
}
