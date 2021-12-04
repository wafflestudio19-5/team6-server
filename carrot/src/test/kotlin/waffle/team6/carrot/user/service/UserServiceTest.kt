package waffle.team6.carrot.user.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import waffle.team6.carrot.user.dto.UserDto
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository

@SpringBootTest
@RunWith(SpringRunner::class)
internal class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @Test
    fun shouldSucceedSignUp() {
        // given
        val signUpRequest = UserDto.SignUpRequest(
            name = "alice",
            password = "alicePassword",
            email = "alice@carrot.com",
            phone = "010-0000-0000"
        )
        val newUser = User(
            name = "alice",
            password = "alicePassword",
            email = "alice@carrot.com",
            phone = "010-0000-0000"
        )
        Mockito.`when`(userRepository.save(Mockito.any(User::class.java)))
            .thenReturn(newUser)

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
}