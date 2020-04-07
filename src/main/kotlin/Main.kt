import dev.arxiv.name.options.SearchField
import dev.arxiv.name.requests.SearchRequest
import dev.arxiv.name.utils.loadAllByRequest
import java.io.FileWriter

fun loadAllByCategory(subjectCategory: String, fileToSave: String) {
    // All article about java
    val request = SearchRequest.SearchRequestBuilder
        .create(subjectCategory, SearchField.SUBJECT_CATEGORY)
        .build()

    loadAllByRequest(request, { feed ->
        // save to file
        val writer = FileWriter(fileToSave, true)
        writer.use {
            feed.entry?.forEach {
                val authorString = it.author.joinToString(", ") { author -> author.name }
                val summary = it.summary.replace("\n", " ")
                val resultString = "${it.updated}|${subjectCategory}|${it.title}|${authorString}|$summary".replace("\n", "")
                writer.write("${resultString}\n")
            }
            writer.close()
        }

        println("parsed ${feed.startIndex + feed.itemsPerPage}/${feed.totalResults}")
        // return false to stop a process of the parsing
        true
    })
}

fun main() {
    val subjectCategory = "cs.SE"
    val fileToSave = "${subjectCategory}WithSummary.csv"

    loadAllByCategory(subjectCategory, fileToSave)
}
