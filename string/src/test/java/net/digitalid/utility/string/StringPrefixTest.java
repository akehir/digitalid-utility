package net.digitalid.utility.string;

import net.digitalid.utility.testing.LoggerSetup;

import org.junit.Assert;
import org.junit.Test;

public class StringPrefixTest extends LoggerSetup {
    
    @Test
    public void testLongestCommonPrefix() {
        Assert.assertEquals("Hello", PrefixString.longestCommonPrefix("Hello world.", "Hello?", "Hello there!"));
        Assert.assertEquals("net.digitalid.", PrefixString.longestCommonPrefix("net.digitalid.utility", "net.digitalid.database", "net.digitalid.core"));
    }
    
}