package waffle.team6

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import waffle.team6.carrot.image.model.Image
import waffle.team6.carrot.image.repository.ImageRepository
import waffle.team6.carrot.location.model.AdjacentLocation
import waffle.team6.carrot.location.model.Location
import waffle.team6.carrot.location.model.RangeOfLocation
import waffle.team6.carrot.location.repository.AdjacentLocationRepository
import waffle.team6.carrot.location.repository.LocationRepository
import waffle.team6.carrot.product.dto.ProductDto
import waffle.team6.carrot.product.model.Category
import waffle.team6.carrot.product.model.CategoryOfInterest
import waffle.team6.carrot.product.repository.CategoryOfInterestRepository
import waffle.team6.carrot.product.repository.LikeRepository
import waffle.team6.carrot.product.repository.ProductRepository
import waffle.team6.carrot.product.repository.PurchaseRequestRepository
import waffle.team6.carrot.product.service.ProductService
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
    private val passwordEncoder: PasswordEncoder,
    private val productService: ProductService
): ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments?) {
        if (locationRepository.count() == (0).toLong()) {
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
                    locationRepository.save(
                        Location(
                            rawLocation[0].toLong(),
                            rawLocation[1],
                            levelZero.toList(),
                            levelOne.toList(),
                            levelTwo.toList(),
                            levelThree.toList()
                        )
                    )
                }
            }
        }

        if (userRepository.count() == (0).toLong()) {
            BufferedReader(InputStreamReader(ClassPathResource("data/example_user.tsv").inputStream)).use { br ->
                br.lines().forEach {
                    val rawUser = it.split("\t")
                    val user = userRepository.save(
                        User(
                            name = rawUser[0],
                            nickname = rawUser[1],
                            email = rawUser[2],
                            phone = rawUser[3],
                            password = passwordEncoder.encode(rawUser[4]),
                            location = rawUser[5],
                            rangeOfLocation = RangeOfLocation.from(rawUser[6].toInt())
                        )
                    )
                    for (i in 1..17) {
                        user.categoriesOfInterest.add(
                            categoryOfInterestRepository.save(CategoryOfInterest(user, Category.from(i)))
                        )
                    }
                }
            }
        }

        if (imageRepository.count() == (0).toLong()) {
            BufferedReader(InputStreamReader(ClassPathResource("data/example_image.tsv").inputStream)).use { br ->
                br.lines().forEach {
                    val rawImage = it.split("\t")
                    imageRepository.save(
                        Image(
                            fileName = rawImage[0],
                            contentType = rawImage[1],
                            userId = rawImage[2].toLong()
                        )
                    )
                }
            }
        }

        if (productRepository.count() == (0).toLong()) {
            BufferedReader(InputStreamReader(ClassPathResource("data/example_product.tsv").inputStream)).use { br ->
                br.lines().forEach { line ->
                    val rawProduct = line.split("\t")
                    val user = userRepository.findByIdOrNull(rawProduct[8].toLong())
                    val postRequest = ProductDto.ProductPostRequest(
                        images = rawProduct[0].split(",").map { it.toLong() },
                        title = rawProduct[1],
                        content = rawProduct[2],
                        price = rawProduct[3].toLong(),
                        negotiable = rawProduct[4].toBoolean(),
                        category = rawProduct[5].toInt(),
                        rangeOfLocation = rawProduct[6].toInt(),
                        forAge = rawProduct[7].toInt()
                    )
                    productService.addProduct(user!!, postRequest)
                }
            }
        }

    }
}