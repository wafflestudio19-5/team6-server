package waffle.team6

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.image.repository.ImageRepository
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.CategoryOfInterest
import waffle.team6.carrot.product.repository.CategoryOfInterestRepository
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.product.repository.PurchaseRequestRepository
import waffle.team6.carrot.user.model.User
import waffle.team6.carrot.user.repository.UserRepository
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class DataLoader(
  private val userRepository: UserRepository,
  private val productRepository: ProductRepository,
  private val purchaseRequestRepository: PurchaseRequestRepository,
  private val likeRepository: LikeRepository,
  private val categoryOfInterestRepository: CategoryOfInterestRepository,
  private val imageRepository: ImageRepository,
  private val passwordEncoder: PasswordEncoder
): ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments?) {
        BufferedReader(InputStreamReader(ClassPathResource("data/example_user.csv").inputStream)).use { br ->
            br.lines().forEach {
                val rawUser = it.split(" ")
                val user = userRepository.save(User(
                    name = rawUser[0],
                    password = passwordEncoder.encode(rawUser[1]),
                    email = rawUser[2],
                    phone = rawUser[3],
                    location = rawUser[4],
                    rangeOfLocation = rawUser[5].toInt()
                ))
                for (i in 1..17) {
                    user.categoriesOfInterest.add(
                        categoryOfInterestRepository.save(CategoryOfInterest(user, Category.from(i)))
                    )
                }
            }
        }


    }
}