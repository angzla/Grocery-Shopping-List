package hu.ait.grocery.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.grocery.data.AppDatabase
import hu.ait.grocery.data.groceryDAO
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class groceryDataModule {
    @Provides
    fun providegroceryDao(appDatabase: AppDatabase): groceryDAO {
        return appDatabase.groceryDao()
    }

    @Provides
    @Singleton
    fun providegroceryAppDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
}