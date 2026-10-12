package com.talktv.aac.player

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

object ContentLauncher {

    private const val TAG = "ContentLauncher"

    // Package names for common TV streaming apps
    private const val PKG_YOUTUBE_KIDS_TV = "com.google.android.youtube.tvkids"
    private const val PKG_YOUTUBE_TV = "com.google.android.youtube.tv"
    private const val PKG_NETFLIX_TV = "com.netflix.ninja"

    /**
     * Attempts to launch content reward directly into installed TV apps or internal player fallback.
     */
    fun launchReward(
        context: Context,
        contentUri: String?,
        title: String,
        preferInternalPlayer: Boolean = false
    ) {
        if (contentUri.isNullOrBlank()) {
            // Launch internal fallback with default sample content
            launchInternalPlayer(context, title, null)
            return
        }

        if (preferInternalPlayer && isDirectVideoUrl(contentUri)) {
            launchInternalPlayer(context, title, contentUri)
            return
        }

        try {
            val uri = Uri.parse(contentUri)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Check if YouTube Kids is installed and URI is YouTube
            if (contentUri.contains("youtube.com") || contentUri.contains("youtu.be")) {
                if (isPackageInstalled(context, PKG_YOUTUBE_KIDS_TV)) {
                    intent.setPackage(PKG_YOUTUBE_KIDS_TV)
                } else if (isPackageInstalled(context, PKG_YOUTUBE_TV)) {
                    intent.setPackage(PKG_YOUTUBE_TV)
                }
            }

            // Verify intent resolution
            val resolvedActivity = intent.resolveActivity(context.packageManager)
            if (resolvedActivity != null) {
                Log.d(TAG, "Launching intent for $title via: ${resolvedActivity.className}")
                context.startActivity(intent)
            } else {
                Log.w(TAG, "No app found to handle URI $contentUri, falling back to internal player")
                launchInternalPlayer(context, title, contentUri)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching external intent for $contentUri", e)
            launchInternalPlayer(context, title, contentUri)
        }
    }

    private fun launchInternalPlayer(context: Context, title: String, videoUri: String?) {
        val playerIntent = Intent(context, VideoPlayerActivity::class.java).apply {
            putExtra(VideoPlayerActivity.EXTRA_VIDEO_TITLE, title)
            putExtra(VideoPlayerActivity.EXTRA_VIDEO_URI, videoUri)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(playerIntent)
    }

    private fun isDirectVideoUrl(uri: String): Boolean {
        val lower = uri.lowercase()
        return lower.endsWith(".mp4") || lower.endsWith(".mkv") || lower.endsWith(".webm") ||
                lower.endsWith(".m3u8") || lower.startsWith("android.resource://") ||
                lower.startsWith("asset://")
    }

    private fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: Exception) {
            false
        }
    }
}
