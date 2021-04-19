package humber.its.drivershigh

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty user name returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `user name already exists returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "123",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "123",
            "",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password does not match with confirmed one return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "123",
            "321",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `password less than 2 digits returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "123",
            "1",
            "1"
        )
        assertThat(result).isFalse()
    }
}