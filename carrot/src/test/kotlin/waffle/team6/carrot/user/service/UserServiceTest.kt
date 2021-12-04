package waffle.team6.carrot.user.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository

@SpringBootTest
@RunWith(SpringRunner::class)
@Rollback
internal class UserServiceTest {

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @BeforeEach
    fun beforeEach() {

    }

    @Test
    fun shouldSucceedSignUp() {
        // given
        val signUpRequest = getSignUpRequestOfAlice()
        val newUser = getUserEntityOfAlice()
        Mockito.`when`(userRepository.save(Mockito.any(User::class.java)))
            .thenReturn(newUser)
        Mockito.`when`(passwordEncoder.encode(Mockito.anyString()))
            .thenReturn("EncodedPassword")

        // when
        val createUser = userService.createUser(signUpRequest)

        // then
        Assertions.assertThat(createUser.name).isEqualTo(signUpRequest.name)
    }

    @Test
    fun shouldSucceedUpdateUser() {
        // given

        // when
//        Mockito.`when`()

        //then
    }

    private fun getSignUpRequestOfAlice(): UserDto.SignUpRequest {
        return UserDto.SignUpRequest(
            name = "alice",
            password = "alicePassword",
            email = "alice@carrot.com",
            phone = "010-0000-0000"
        )
    }

    private fun getUserEntityOfAlice(): User {
        return User(
            name = "alice",
            password = "EncodedPassword",
            email = "alice@carrot.com",
            phone = "010-0000-0000"
        )
    }
}