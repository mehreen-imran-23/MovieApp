package com.example.movieapp.data.local
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters


class PassHash {

    suspend fun hash(password: String): HashedPass =
        withContext(Dispatchers.Default) {
            val salt = ByteArray(16)
            SecureRandom().nextBytes(salt)

            val hash = genHash(password, salt)

            HashedPass(
                hash = Base64.encodeToString(hash, Base64.NO_WRAP),
                salt = Base64.encodeToString(salt, Base64.NO_WRAP),
            )
        }

    suspend fun verify(
        password: String,
        savedHash: String,
        savedSalt: String,
    ): Boolean = withContext(Dispatchers.Default) {
        val salt = Base64.decode(savedSalt, Base64.NO_WRAP)
        val expectedHash = Base64.decode(savedHash, Base64.NO_WRAP)
        val actualHash = genHash(password, salt)

        MessageDigest.isEqual(expectedHash, actualHash)
    }

    private fun genHash(password: String, salt: ByteArray,): ByteArray
    {
        val param = Argon2Parameters.Builder(
            Argon2Parameters.ARGON2_id,
        )
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withMemoryAsKB(19_456)
            .withIterations(2)
            .withParallelism(1)
            .withSalt(salt)
            .build()

        val gen = Argon2BytesGenerator()
        gen.init(param)

        val passChars = password.toCharArray()

        return try
        {
            ByteArray(32).also { output ->
                gen.generateBytes(passChars, output)
            }
        }
        finally
        {
            passChars.fill('\u0000')
        }
    }
}