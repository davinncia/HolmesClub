package com.davinciapp.holmesclub.view.drafts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.davinciapp.holmesclub.repository.DraftRepository
import com.davinciapp.holmesclub.utlis.getOrAwaitValue
import com.davinciapp.holmesclub.view.drafts.model.Draft
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DraftViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var draftRepo: DraftRepository
    private lateinit var viewModel: DraftViewModel

    @Before
    fun setUp() {
        Mockito.`when`(draftRepo.getAllDrafts()).thenReturn(MutableLiveData(dumDrafts))
        viewModel = DraftViewModel(draftRepo)
    }

    @Test
    fun draftItemsAreMappedCorrectly() {
        //GIVEN setup
        //WHEN
        val items = viewModel.draftItems.getOrAwaitValue()
        //THEN
        Assert.assertEquals(2, items.size)
        Assert.assertEquals("Draft One", items[0].title)
        Assert.assertEquals("Draft Two", items[1].title)
    }

    @Test
    fun modifTimeIsCorrectlyTranslated() {
        //GIVEN setup
        //WHEN
        val items = viewModel.draftItems.getOrAwaitValue()
        //THEN
        Assert.assertEquals(2, items.size)
        Assert.assertEquals("10-07 03:11", items[0].modifTime)
        Assert.assertEquals(5, items[1].modifTime.length) //Day not precised if today
    }

    //DUMMY DRAFTS
    private val dumDrafts = listOf<Draft>(
        Draft("Draft One", "content one", 11, 111111111111L, "picOneUri"),
        Draft("Draft Two", "content two", 22, System.currentTimeMillis() - 60_000, "picTwoUri")
    )

}