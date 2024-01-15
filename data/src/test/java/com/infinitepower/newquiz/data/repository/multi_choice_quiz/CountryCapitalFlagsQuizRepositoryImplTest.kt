package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.data.repository.country.TestCountryRepositoryImpl
import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [CountryCapitalFlagsQuizRepositoryImpl]
 */
internal class CountryCapitalFlagsQuizRepositoryImplTest {
    private val countryRepository: CountryRepository = TestCountryRepositoryImpl()

    private lateinit var repository: CountryCapitalFlagsQuizRepositoryImpl

    @BeforeTest
    fun setUp() {
        repository = CountryCapitalFlagsQuizRepositoryImpl(
            countryRepository = countryRepository
        )
    }

    @Test
    fun `getFlagQuiz() returns a list of questions`() = runTest {
        val questionSize = 5

        val questions = repository.getRandomQuestions(
            amount = questionSize,
            category = MultiChoiceBaseCategory.CountryCapitalFlags
        )

        assertThat(questions).hasSize(questionSize)

        // Use imageUrl because it is unique for each question
        val uniqueQuestions = questions.distinctBy { it.image }
        assertThat(uniqueQuestions).hasSize(questionSize)

        // Check if the questions category is correct
        assertThat(questions.all { it.category == MultiChoiceBaseCategory.CountryCapitalFlags }).isTrue()

        val allCountries = countryRepository.getAllCountries()

        // Check if the answer capital corresponds to the country
        questions.forEach { question ->
            val capitalAnswer = question.answers[question.correctAns]

            val countryName = question
                .description
                .removePrefix("What is the capital of ")
                .removeSuffix("?")

            val country = allCountries.find { it.countryName == countryName }
            assertThat(country).isNotNull()
            require(country != null)

            assertThat(capitalAnswer).isEqualTo(country.capital)
        }
    }

    @ParameterizedTest(name = "getRandomQuestions returns questions filtered by difficulty: {0}")
    @ValueSource(strings = ["easy", "medium", "hard"])
    fun `getRandomQuestions() returns questions filtered by difficulty`(
        difficulty: String
    ) = runTest {
        val questionSize = 5

        val questions = repository.getRandomQuestions(
            amount = questionSize,
            category = MultiChoiceBaseCategory.CountryCapitalFlags,
            difficulty = difficulty
        )

        assertThat(questions).hasSize(questionSize)

        // Check if the questions difficulty is correct
        assertThat(questions.all { it.difficulty == QuestionDifficulty.from(difficulty) }).isTrue()
    }
}
