package io.github.simonscholz.github.release.notes

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GitHubApi {

    @GET("repos/{owner}/{project}/pulls?state=closed&base=master&sort=updated&direction=desc&per_page=100")
    fun getLatestUpdatedPullRequests(
        @Path("owner") owner: String,
        @Path("project") project: String,
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String,
    ): Call<List<PullRequest>>

    @GET("repos/{owner}/{project}/releases?per_page=1")
    fun getLatestRelease(
        @Path("owner") owner: String,
        @Path("project") project: String,
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String,
    ): Call<List<Release>>

    @POST("repos/{owner}/{project}/releases")
    fun createRelease(
        @Path("owner") owner: String,
        @Path("project") project: String,
        @Header("Authorization") authorization: String,
        @Header("Accept") accept: String,
        @Body releaseCreationPayload: ReleaseCreationPayload,
    ): Call<Any>

    companion object {
        var BASE_URL = "https://api.github.com/"

        fun create(): GitHubApi {
            // 2021-06-29T11:53:14Z
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(GitHubApi::class.java)
        }
    }
}