package humber.its.drivershigh

object RegistrationUtil {

    private val existingUsers = listOf("Peter", "Carl")

    /**
     * the input is not valid if,
     *      the userName/password is empty,
     *      the userName is already taken,
     *      the confirmedPassword is not the same as the real password,
     *      the password contains less than 2 digits
     */
    fun validateRegistrationInput(
        userName: String,
        password: String,
        confirmedPassword: String
    ): Boolean {
        if (userName.isEmpty() || password.isEmpty()) {
            return false
        }

        if (userName in existingUsers) {
            return false
        }

        if (password != confirmedPassword) {
            return false
        }

        if (password.count { it.isDigit() } < 2) {
            return false
        }

        return true
    }
}