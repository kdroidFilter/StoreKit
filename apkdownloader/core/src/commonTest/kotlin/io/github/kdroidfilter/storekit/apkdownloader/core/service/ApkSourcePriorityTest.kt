package io.github.kdroidfilter.storekit.apkdownloader.core.service

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ApkSourcePriorityTest {

    @Test
    fun testDefaultPriorityOrder() {
        // Given the default priority order
        
        // When getting the priority order
        val priorityOrder = ApkSourcePriority.getPriorityOrder()
        
        // Then the default order should be APKCOMBO, APTOIDE
        assertEquals(2, priorityOrder.size, "Priority order should have 2 elements")
        assertEquals(ApkSource.APKCOMBO, priorityOrder[0], "First source should be APKCOMBO")
        assertEquals(ApkSource.APTOIDE, priorityOrder[1], "Second source should be APTOIDE")
    }
    
    @Test
    fun testSetPriorityOrder() {
        try {
            // Given a new priority order
            val newOrder = listOf(ApkSource.APTOIDE, ApkSource.APKCOMBO)
            
            // When setting the priority order
            ApkSourcePriority.setPriorityOrder(newOrder)
            
            // Then the priority order should be updated
            val priorityOrder = ApkSourcePriority.getPriorityOrder()
            assertEquals(2, priorityOrder.size, "Priority order should have 2 elements")
            assertEquals(ApkSource.APTOIDE, priorityOrder[0], "First source should be APTOIDE")
            assertEquals(ApkSource.APKCOMBO, priorityOrder[1], "Second source should be APKCOMBO")
        } finally {
            // Reset to default for other tests
            ApkSourcePriority.resetToDefault()
        }
    }
    
    @Test
    fun testResetToDefault() {
        // Given a modified priority order
        ApkSourcePriority.setPriorityOrder(listOf(ApkSource.APTOIDE, ApkSource.APKCOMBO))
        
        // When resetting to default
        ApkSourcePriority.resetToDefault()
        
        // Then the priority order should be the default
        val priorityOrder = ApkSourcePriority.getPriorityOrder()
        assertEquals(2, priorityOrder.size, "Priority order should have 2 elements")
        assertEquals(ApkSource.APKCOMBO, priorityOrder[0], "First source should be APKCOMBO")
        assertEquals(ApkSource.APTOIDE, priorityOrder[1], "Second source should be APTOIDE")
    }
    
    @Test
    fun testSetEmptyPriorityOrder() {
        // When setting an empty priority order
        // Then an exception should be thrown
        assertFailsWith<IllegalArgumentException> {
            ApkSourcePriority.setPriorityOrder(emptyList())
        }
    }
    
    @Test
    fun testSetDuplicatePriorityOrder() {
        // When setting a priority order with duplicates
        // Then an exception should be thrown
        assertFailsWith<IllegalArgumentException> {
            ApkSourcePriority.setPriorityOrder(listOf(ApkSource.APKCOMBO, ApkSource.APKCOMBO))
        }
    }
    
    @Test
    fun testSetIncompletePriorityOrder() {
        // When setting a priority order that doesn't include all sources
        // Then an exception should be thrown
        assertFailsWith<IllegalArgumentException> {
            ApkSourcePriority.setPriorityOrder(listOf(ApkSource.APKCOMBO))
        }
    }
}