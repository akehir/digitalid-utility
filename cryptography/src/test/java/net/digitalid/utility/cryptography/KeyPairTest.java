package net.digitalid.utility.cryptography;

import javax.annotation.Nonnull;

import net.digitalid.utility.logging.Log;
import net.digitalid.utility.math.Element;
import net.digitalid.utility.testing.LoggerSetup;
import net.digitalid.utility.time.Time;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit testing of the class {@link KeyPair}.
 */
public class KeyPairTest extends LoggerSetup {
    
    @Test
    public void testKeyPair() {
        @Nonnull Time time = Time.getCurrent();
        final @Nonnull KeyPair keyPair = KeyPair.getRandom();
        final @Nonnull PrivateKey privateKey = keyPair.getPrivateKey();
        final @Nonnull PublicKey publicKey = keyPair.getPublicKey();
        Log.information("Key Pair Generation: " + time.ago().getValue() + " ms");
        
        Assert.assertTrue(publicKey.verifySubgroupProof());
        
        for (int i = 0; i < 10; i++) {
            Log.information("Starting with another round:");
            final @Nonnull Element m = publicKey.getCompositeGroup().getRandomElement();
            time = Time.getCurrent();
            final @Nonnull Element c = m.pow(publicKey.getE());
            Log.information("Encryption (only algorithm): " + time.ago().getValue() + " ms");
            time = Time.getCurrent();
            Assert.assertEquals(c.pow(privateKey.getD()), m);
            Log.information("Decryption (slow algorithm): " + time.ago().getValue() + " ms");
            time = Time.getCurrent();
            Assert.assertEquals(privateKey.powD(c), m);
            Log.information("Decryption (fast algorithm): " + time.ago().getValue() + " ms");
        }
    }
    
}