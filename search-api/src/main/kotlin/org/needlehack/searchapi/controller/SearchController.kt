package org.needlehack.searchapi.controller

import org.needlehack.searchapi.model.FeedItem
import io.searchbox.client.JestClient
import io.searchbox.core.Search
import io.searchbox.core.SearchResult
import org.elasticsearch.index.query.DisMaxQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController {

	@Autowired
	private val client: JestClient? = null

	@Value("\${jest.elasticsearch.index}")
	private var indexName: String? = "feed-collector"

	@GetMapping("/search")
	fun search(@RequestParam(value = "term", defaultValue = "*") term: String): List<SearchResult.Hit<FeedItem, Void>>? {

		System.out.println("You want to seach: $term")

		val searchSourceBuilder = SearchSourceBuilder()
		searchSourceBuilder.query(QueryBuilders.disMaxQuery().add(QueryBuilders.termQuery("topics", term)).add(QueryBuilders.termQuery("title", term)).tieBreaker(0.3f))

		val search: Search.Builder = Search.Builder(searchSourceBuilder.toString())
		if (indexName != null) search.addIndex(indexName)

		val result = client?.execute(search.build())
		val resultList = result?.getHits(FeedItem::class.java)

		System.out.println("found: ${resultList?.size}")

		return resultList
	}
}