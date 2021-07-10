package io.github.eh.eh

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.eh.eh.http.HTTPBootstrap
import io.github.eh.eh.http.HTTPContext
import io.github.eh.eh.http.StreamHandler
import io.github.eh.eh.http.bundle.ResponseBundle
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.serialization.ClassResolvers
import io.netty.handler.codec.serialization.ObjectDecoder
import io.netty.handler.codec.serialization.ObjectEncoder

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val boot:HTTPBootstrap = HTTPBootstrap.builder().host(HTTPBootstrap.HTTP_LOGIN).port(1300)
            .streamHandler(object : StreamHandler {
                override fun onRead(bundle: ResponseBundle?) {
                    TODO("Not yet implemented")
                }

                override fun onWrite(outputStream: HTTPContext?) {

                    TODO("Not yet implemented")
                }
        }).build();
    }
}