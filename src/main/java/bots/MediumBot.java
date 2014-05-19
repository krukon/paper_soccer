package bots;

import java.util.concurrent.Callable;


public final class MediumBot extends StrategyBot {
	public static final String name ="Medium bot";


	
	@Override
	public String getName() {
		return name;
	}

	public MediumBot() {
		super();
		addStrategy(0.39, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeOffensiveStrategy();
				return null;
			}
		});
		addStrategy(0.02, new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				computeRandomStrategy();
				return null;
			}
		});
		addStrategy(0.59, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeDefensiveStrategy();
				return null;
			}
		});
	}
	
}