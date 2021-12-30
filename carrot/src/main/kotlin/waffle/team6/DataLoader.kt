package waffle.team6

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.image.repository.ImageRepository
import waffle.team6.carrot.location.model.AdjacentLocation
import waffle.team6.carrot.location.model.Location
import waffle.team6.carrot.location.repository.AdjacentLocationRepository
import waffle.team6.carrot.location.repository.LocationRepository
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
    private val locationRepository: LocationRepository,
    private val adjacentLocationRepository: AdjacentLocationRepository,
    private val passwordEncoder: PasswordEncoder
): ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments?) {
        BufferedReader(InputStreamReader(ClassPathResource("data/location.tsv").inputStream)).use { br ->
            br.lines().forEach {
                val rawLocation = it.split("\t")
                val levelZero = mutableListOf<AdjacentLocation>()
                val levelOne = mutableListOf<AdjacentLocation>()
                val levelTwo = mutableListOf<AdjacentLocation>()
                val levelThree = mutableListOf<AdjacentLocation>()
                val levelZeroLocations = rawLocation[2].split(",")
                val levelOneLocations = rawLocation[3].split(",")
                val levelTwoLocations = rawLocation[4].split(",")
                val levelThreeLocations = rawLocation[5].split(",")
                for (adjacent in levelZeroLocations)
                    levelZero.add(adjacentLocationRepository.save(AdjacentLocation(name = adjacent)))
                for (adjacent in levelOneLocations)
                    levelOne.add(adjacentLocationRepository.save(AdjacentLocation(name = adjacent)))
                for (adjacent in levelTwoLocations)
                    levelTwo.add(adjacentLocationRepository.save(AdjacentLocation(name = adjacent)))
                for (adjacent in levelThreeLocations)
                    levelThree.add(adjacentLocationRepository.save(AdjacentLocation(name = adjacent)))
                locationRepository.save(Location(
                    rawLocation[0].toLong(),
                    rawLocation[1],
                    levelZero.toList(),
                    levelOne.toList(),
                    levelTwo.toList(),
                    levelThree.toList()
                ))
            }
        }
        
        BufferedReader(InputStreamReader(ClassPathResource("data/example_user.csv").inputStream)).use { br ->
            br.lines().forEach {
                val rawUser = it.split(" ")
                val user = userRepository.save(User(
                    name = rawUser[0],
                    nickname = rawUser[0],
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