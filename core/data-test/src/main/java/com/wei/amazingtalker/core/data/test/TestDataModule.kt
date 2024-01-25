package com.wei.amazingtalker.core.data.test

import com.wei.amazingtalker.core.data.di.DataModule
import com.wei.amazingtalker.core.data.repository.DefaultTeacherScheduleRepository
import com.wei.amazingtalker.core.data.repository.DefaultUserDataRepository
import com.wei.amazingtalker.core.data.repository.ProfileRepository
import com.wei.amazingtalker.core.data.repository.TeacherScheduleRepository
import com.wei.amazingtalker.core.data.repository.UserDataRepository
import com.wei.amazingtalker.core.data.repository.fake.FakeProfileRepository
import com.wei.amazingtalker.core.data.utils.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
interface TestDataModule {
    @Binds
    fun bindsProfileRepository(profileRepository: FakeProfileRepository): ProfileRepository

    @Binds
    fun bindsTeacherScheduleRepository(teacherScheduleRepository: DefaultTeacherScheduleRepository): TeacherScheduleRepository

    @Binds
    fun bindsNetworkMonitor(networkMonitor: AlwaysOnlineNetworkMonitor): NetworkMonitor

    @Binds
    fun bindsUserDataRepository(userDataRepository: DefaultUserDataRepository): UserDataRepository
}