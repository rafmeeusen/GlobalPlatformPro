package pro.javacard.gp.tests;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import pro.javacard.gp.GPData;
import pro.javacard.gp.GPKeySet;
import pro.javacard.gp.GPKeySet.GPKey;
import pro.javacard.gp.GPKeySet.GPKey.Type;
import pro.javacard.gp.GPUtils;
import pro.javacard.gp.GlobalPlatform;
import apdu4j.TerminalManager;
import ds.javacard.emulator.jcop.DS_provider;

/**
 * Base class for tests running against JCOP simulator.
 * Test setup will add a key version 1 with so called "old keys" (instead of the factory key version 255).
 * */
public abstract class TestJCOPSimul {

	static GlobalPlatform gp;
	static Card card;
	static CardChannel chan =null;
	final static int port = 1234;

	final static GPKey old_isdkey1_1 = new GPKey(GPUtils.hexStringToByteArray("2b11e9f7f27d9c92e220e2ed8c8feb9f"), Type.DES3);
	final static GPKey old_isdkey1_2 = new GPKey(GPUtils.hexStringToByteArray("9e27ced733799bc197add68dd83348eb"), Type.DES3);
	final static GPKey old_isdkey1_3 = new GPKey(GPUtils.hexStringToByteArray("4d96d585a546ec1364efe7c0efc0bcb0"), Type.DES3);

	final static GPKey new_isdkey1_1 = new GPKey(GPUtils.hexStringToByteArray("f905e7a82d13bfc3c849da436df8f6a0"), Type.DES3);
	final static GPKey new_isdkey1_2 = new GPKey(GPUtils.hexStringToByteArray("9135d6e04f77db03ad6ad9212ec4639b"), Type.DES3);
	final static GPKey new_isdkey1_3 = new GPKey(GPUtils.hexStringToByteArray("df5fd7be121d0b0a2b70f60305027670"), Type.DES3);

	/**
	 * Setup JCOP simulator to behave as pre-personalized card. Then reset and reconnect.
	 * */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DS_provider p = new DS_provider();
		Security.addProvider(p);
		int[] ports = new int[]{ port };
		TerminalFactory factory = TerminalFactory.getInstance("JcopEmulator", ports);
		CardTerminal terminal = factory.terminals().list().get(0);
		card = terminal.connect("T=1");

		/* auth & put key set */
		gp = new GlobalPlatform(card.getBasicChannel());
		gp.select(null);
		gp.openSecureChannel(new GPKeySet(GPData.defaultKey), null, GlobalPlatform.SCP_ANY, GlobalPlatform.defaultMode);
		List<GPKeySet.GPKey> nxp_provisioned_keys = new ArrayList<GPKeySet.GPKey>();
		nxp_provisioned_keys.add(new GPKey(01, 01, old_isdkey1_1));
		nxp_provisioned_keys.add(new GPKey(01, 02, old_isdkey1_2));
		nxp_provisioned_keys.add(new GPKey(01, 03, old_isdkey1_3));
		gp.putKeys(nxp_provisioned_keys, false);

		/* reset card */
		card.disconnect(true);
		card = terminal.connect("T=1");
		gp = new GlobalPlatform(card.getBasicChannel());
		gp.select(null);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TerminalManager.disconnect(card, true);
	}

}
