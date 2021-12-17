package io.github.eh.eh.http.cipher

import android.util.Base64
import io.github.eh.eh.ioutils.IOUtils
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CipherBase private constructor() {
    private val PRIVATE_KEY = "Vaf6vj6MmVo1NIUbKfk1SfXx3JGdM48B"

    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun encode(inputStream: InputStream?): ByteArray {
        val data = IOUtils.readAllBytes(inputStream)
        val secretKey: SecretKey = SecretKeySpec(data, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(
                PRIVATE_KEY.substring(0, 16).toByteArray(
                    StandardCharsets.UTF_8
                )
            )
        )
        val encoded = Base64.encode(data, Base64.DEFAULT)
        return cipher.doFinal(encoded)
    }

    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun encode(data: ByteArray?): ByteArray {
        val secretKey: SecretKey =
            SecretKeySpec(PRIVATE_KEY.toByteArray(StandardCharsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(
                PRIVATE_KEY.substring(0, 16).toByteArray(
                    StandardCharsets.UTF_8
                )
            )
        )
        return cipher.doFinal(data)
    }

    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun decode(inputStream: InputStream?): InputStream {
        val secretKey: SecretKey =
            SecretKeySpec(PRIVATE_KEY.toByteArray(Charset.forName("UTF8")), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(
                PRIVATE_KEY.substring(0, 16).toByteArray(
                    StandardCharsets.UTF_8
                )
            )
        )
        return CipherInputStream(inputStream, cipher)
    }
    fun decode(input:String): String {
        val secretKey: SecretKey =
            SecretKeySpec(PRIVATE_KEY.toByteArray(Charset.forName("UTF8")), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(
                PRIVATE_KEY.substring(0, 16).toByteArray(
                    StandardCharsets.UTF_8
                )
            )
        )
        var de = Base64.decode(input,Base64.DEFAULT)
        return String(cipher.doFinal(de),Charset.forName("UTF-8"))
    }
    companion object {
        @JvmStatic
        var instance: CipherBase? = null
            get() = if (field == null) CipherBase().also { field = it } else field
            private set
    }
}