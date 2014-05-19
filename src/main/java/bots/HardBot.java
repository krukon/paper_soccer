package bots;

import java.util.concurrent.Callable;

public final class HardBot extends StrategyBot {
	public static final String name ="Hard bot";


	
	@Override
	public String getName() {
		return name;
	}

	public HardBot() {
		super();
		addStrategy(0.78, new Callable<Void>() {

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
		addStrategy(0.20, new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				computeDefensiveStrategy();
				return null;
			}
		});
	}
	
}