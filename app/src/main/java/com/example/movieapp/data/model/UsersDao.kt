package com.example.movieapp.data.model

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.movieapp.data.local.AppPreferencesEntity
import com.example.movieapp.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?

    @Query("SELECT * FROM app_preferences WHERE id = 1")
    suspend fun getPreferences(): AppPreferencesEntity?

    @Upsert
    suspend fun savePreferences(preferences: AppPreferencesEntity)

    @Query("SELECT * FROM app_preferences WHERE id = 1")
    fun observePreferences(): Flow<AppPreferencesEntity?>

    @Query(
        """
        UPDATE users
        SET name = :name,
            phoneNumber = :phoneNumber,
            city = :city
        WHERE id = :userId
    """
    )
    suspend fun updateProfile(
        userId: Long, name: String, phoneNumber: String,
        city: String
    ): Int
}