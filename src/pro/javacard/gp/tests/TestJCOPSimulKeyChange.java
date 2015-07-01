package pro.javacard.gp.tests;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.CardException;

import org.junit.Test;

import pro.javacard.gp.GPData;
import pro.javacard.gp.GPException;
import pro.javacard.gp.GPKeySet;
import pro.javacard.gp.GPKeySet.GPKey;
import pro.javacard.gp.GlobalPlatform;

public class TestJCOPSimulKeyChange extends TestJCOPSimul {

	public static byte[] newKey = { 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x5B, 0x5C, 0x5D, 0x5E, 0x5F };

	@Test
	public void testReplaceKeyset() throws CardException, GPException {
		gp.beVerboseTo(System.out);
		// auth with 'old' keys
		GPKeySet nxp_provisioned_keys = new GPKeySet(new GPKey(01, 01, old_isdkey1_1), new GPKey(01, 02, old_isdkey1_2), new GPKey(01, 03, old_isdkey1_3) );
		gp.openSecureChannel(nxp_provisioned_keys, null, GlobalPlatform.SCP_ANY, GlobalPlatform.defaultMode);

		// putkey new keys
		List<GPKeySet.GPKey> newkeys = new ArrayList<GPKeySet.GPKey>();
		newkeys.add(new GPKey(01, 01, new_isdkey1_1));
		newkeys.add(new GPKey(01, 02, new_isdkey1_2));
		newkeys.add(new GPKey(01, 03, new_isdkey1_3));

		GPData.print_card_info(gp);
		gp.putKeys(newkeys, true); // replace = true ??
		GPData.print_card_info(gp);
	}

}
