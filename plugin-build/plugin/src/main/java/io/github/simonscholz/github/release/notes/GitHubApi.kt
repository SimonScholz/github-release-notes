package io.github.simonscholz.github.release.notes

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubApi {

    @GET("repos/{owner}/{project}/pulls?state=closed&base=master&sort=updated&direction=desc&per_page=100")
    fun getLatestUpdatedPullRequests(
        @Path("owner") owner: String,
        @Path("project") project: String,
    ): Call<List<PullRequest>>

    @GET("repos/{owner}/{project}/releases?per_page=1")
    fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("project") project: String,
    ): Call<List<Release>>

    @POST("repos/{owner}/{project}/releases")
    fun createRelease(
        @Path("owner") owner: String,
        @Path("project") project: String,
        @Body releaseCreationRequestPayload: ReleaseCreationRequestPayload,
    ): Call<ReleaseCreationResponsePayload>

    companion object {
        var BASE_URL = "https://api.github.com/"

        fun create(authorization: String): GitHubApi {
            val httpClient = OkHttpClient.Builder().addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .addHeader("Authorization", authorization)
                    .build()
                it.proceed(request)
            }
            // 2021-06-29T11:53:14Z
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build()
            return retrofit.create(GitHubApi::class.java)
        }
    }
}
