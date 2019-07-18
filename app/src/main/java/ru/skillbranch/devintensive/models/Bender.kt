package ru.skillbranch.devintensive.models

import android.util.Log

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {
    var wrongAnswerCount = 0 // Int

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val validateError = validate(answer)
        Log.d("M_MainActivity", "listenAnswer error: $validateError")

        return if (question == Question.IDLE) {
            question.question to status.color
        } else if (validateError != null) {
            "$validateError\n${question.question}" to status.color
        } else if (question.answers.contains(answer.toLowerCase())) {
            wrongAnswerCount = 0
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            // TODO - при вопросе IDLE не надо накручивать неправильные ответы
            wrongAnswerCount++
            if (wrongAnswerCount < 3) {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            } else {
                status = Status.NORMAL
                question = Question.NAME
                wrongAnswerCount = 0
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        // fun nextStatus(): Status = values()[(ordinal + 1) % values().lastIndex]
        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion() = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion() = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion() = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion() = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion() = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion() = IDLE
        };

        abstract fun nextQuestion(): Question
    }

    // TODO - слить в enum Question
    private fun validate(answer: String): String? {
        var error : String? = null

        when (question) {
            Question.NAME -> {
                if (answer.isBlank() || !answer[0].isUpperCase()) {
                    error = "Имя должно начинаться с заглавной буквы"
                }
            }
            Question.PROFESSION -> {
                if (answer.isBlank() || !answer[0].isLowerCase()) {
                    error = "Профессия должна начинаться со строчной буквы"
                }
            }
            Question.MATERIAL -> {
                // val materialRegex = Regex("[^\\d]+")
                val materialRegex = Regex("\\d+")
                if (answer.isBlank() || materialRegex.containsMatchIn(answer)) {
                    error = "Материал не должен содержать цифр"
                }
            }
            Question.BDAY -> {
                val bdayRegex = Regex("\\d+")
                if (answer.isBlank() || !bdayRegex.matches(answer)) {
                    error = "Год моего рождения должен содержать только цифры"
                }
            }
            Question.SERIAL -> {
                val serialRegex = Regex("\\d+")
                if (answer.isBlank() || !serialRegex.matches(answer) || answer.length != 7) {
                    error = "Серийный номер содержит только цифры, и их 7"
                }
            }
            Question.IDLE -> {}
        }
        return error
    }
}
