package io.muoncore.transport.tcp;

import io.muoncore.config.AutoConfiguration;
import io.muoncore.transport.MuonTransport;
import io.muoncore.transport.MuonTransportFactory;

import java.util.Properties;

public class TCPTransportFactory implements MuonTransportFactory {
    @Override
    public MuonTransport build(Properties properties) {
        return new TCPTransport();
    }

    @Override
    public void setAutoConfiguration(AutoConfiguration autoConfiguration) {

    }
}
