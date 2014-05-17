package bots;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import helpers.Player;

public class BotLoader {

	public static class BotLoaderException extends Exception {
		private static final long serialVersionUID = 4526796041843500347L;
		public final String botName;
		
		public BotLoaderException(String botName) {
			this.botName = botName;
		}
	}

	private static Map<String, Class<? extends Player>> registeredBots = new TreeMap<>();
	private static List<String> botsIds = new ArrayList<>();

	static {
		registerBot("Random bot", RandomBot.class);
		registerBot("Easy Bot", EasyBot.class);
	}

	/**
	 * Constructs a Player object from the specified id representing a bot.
	 * The class of such bot has to be already registered in BotLoader.
	 *
	 * @param id registered id of the bot
	 * @throws BotLoaderException thrown if specified bot cannot be created
	 */
	public static Player loadBot(String id) throws BotLoaderException {
		Class<? extends Player> botClass = registeredBots.get(id);
		try {
			return botClass.newInstance();
		} catch (Exception e) {
			throw new BotLoaderException(id);
		}
	}

	/**
	 * Get the list of the ids of all registered bots in BotLoader.
	 */
	public static List<String> getBotsIds() {
		return new ArrayList<>(botsIds);
	}

	/**
	 * Register new bot in BotLoader. All registered bots are available
	 * in single player mode.
	 *
	 * @param id identificator of the bot - human readable form
	 * @param bot class implementing Player interface representing the bot
	 */
	private static void registerBot(String id, Class<? extends Player> bot) {
		botsIds.add(id);
		registeredBots.put(id, bot);
	}
}
