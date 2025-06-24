package com.controller.controller_authorization;

import com.dao.UserDao;
import com.entity.User;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for managing user permissions with advanced caching and
 * security features.
 *
 * Features: - Time-based cache expiration - Background cache cleanup -
 * Permission validation - Security logging
 *
 * @author vinhnnpc
 */
public class PermissionManager {

    private static final Logger LOGGER = Logger.getLogger(PermissionManager.class.getName());
    private static final ConcurrentHashMap<String, CachedPermission> PERMISSION_CACHE = new ConcurrentHashMap<>();
    private static final int CACHE_TTL_MINUTES = 30;
    private static final ScheduledExecutorService CLEANUP_SCHEDULER = Executors.newScheduledThreadPool(1);

    private final UserDao userDao;

    static {
        // Schedule cache cleanup every 10 minutes
        CLEANUP_SCHEDULER.scheduleAtFixedRate(PermissionManager::cleanupExpiredCache, 10, 10, TimeUnit.MINUTES);

        // Add shutdown hook to clean up scheduler
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CLEANUP_SCHEDULER.shutdown();
            try {
                if (!CLEANUP_SCHEDULER.awaitTermination(5, TimeUnit.SECONDS)) {
                    CLEANUP_SCHEDULER.shutdownNow();
                }
            } catch (InterruptedException e) {
                CLEANUP_SCHEDULER.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));
    }

    public PermissionManager() {
        this.userDao = new UserDao();
    }

    /**
     * Checks if a user has permission to access a specific endpoint.
     *
     * @param user the user to check
     * @param endpoint the endpoint to check permission for
     * @return true if user has permission, false otherwise
     */
    public boolean hasPermission(User user, String endpoint) {
        if (user == null || endpoint == null || endpoint.trim().isEmpty()) {
            LOGGER.warning("Invalid parameters for permission check - user: " + user + ", endpoint: " + endpoint);
            return false;
        }

        String cacheKey = generateCacheKey(String.valueOf(user.getUserId()), endpoint);

        // Check cache first
        CachedPermission cached = PERMISSION_CACHE.get(cacheKey);
        if (cached != null && !cached.isExpired()) {
            LOGGER.fine("Cache hit for permission check - user: " + user.getUserId() + ", endpoint: " + endpoint);
            return cached.hasPermission();
        }

        // Check database
        boolean hasPermission = userDao.hasPermission(user.getUserId(), endpoint);

        // Cache the result
        PERMISSION_CACHE.put(cacheKey, new CachedPermission(hasPermission));

        // Log the permission check
        LOGGER.info("Permission check - user: " + user.getUserId()
                + ", endpoint: " + endpoint + ", result: " + hasPermission);

        return hasPermission;
    }

    /**
     * Invalidates all cached permissions for a specific user. Useful when user
     * permissions are updated.
     *
     * @param userId the user ID to invalidate cache for
     */
    public void invalidateUserCache(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return;
        }

        PERMISSION_CACHE.entrySet().removeIf(entry -> entry.getKey().startsWith(userId + ":"));
        LOGGER.info("Cache invalidated for user: " + userId);
    }

    /**
     * Clears all cached permissions.
     */
    public static void clearAllCache() {
        PERMISSION_CACHE.clear();
        LOGGER.info("All permission cache cleared");
    }

    /**
     * Gets cache statistics for monitoring.
     *
     * @return cache statistics
     */
    public static CacheStats getCacheStats() {
        return new CacheStats(PERMISSION_CACHE.size());
    }

    /**
     * Generates a cache key for user and endpoint combination.
     */
    private String generateCacheKey(String userId, String endpoint) {
        return userId + ":" + endpoint;
    }

    /**
     * Removes expired cache entries.
     */
    private static void cleanupExpiredCache() {
        try {
            int beforeSize = PERMISSION_CACHE.size();
            PERMISSION_CACHE.entrySet().removeIf(entry -> entry.getValue().isExpired());
            int afterSize = PERMISSION_CACHE.size();

            if (beforeSize != afterSize) {
                LOGGER.info("Cache cleanup removed " + (beforeSize - afterSize) + " expired entries");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during cache cleanup", e);
        }
    }

    /**
     * Inner class representing a cached permission with expiration.
     */
    private static class CachedPermission {

        private final boolean hasPermission;
        private final long timestamp;

        public CachedPermission(boolean hasPermission) {
            this.hasPermission = hasPermission;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean hasPermission() {
            return hasPermission;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > TimeUnit.MINUTES.toMillis(CACHE_TTL_MINUTES);
        }
    }

    /**
     * Statistics class for cache monitoring.
     */
    public static class CacheStats {

        private final int size;

        public CacheStats(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }
}
