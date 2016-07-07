package wlj;

import java.util.Random;

public class BasketballSimulation {
	public static void main(String[] args) {
		Team la = new Team();
		la.name = "湖人";
		la.s2 = true;
		la.shooting2 = 1;
		Team bo = new Team();
		bo.name = "凯尔特人";
		bo.s2 = false;
		bo.shooting3 = 2 / 3f;

		Random random = new Random();

		Game game = new Game(la, bo);

		for (int i = 0; i < 100; i++) {
			ShootResult shootResult = game.shoot();
			if (shootResult == ShootResult.zero) {
				if (random.nextFloat() > 0.1) {
					game.swap();
				}
			} else if (shootResult == ShootResult.tow) {
				game.swap();
			} else if (shootResult == ShootResult.three) {
				game.swap();
			}
		}
		printGameInfo(la);
		printGameInfo(bo);
	}

	/**
	 * @param team
	 */
	private static void printGameInfo(Team team) {
		System.out.println(team.name + "得分：" + team.score);
		System.out.println(team.name + "攻击次数：" + team.attackCount);
		System.out.println(team.name + "2分球命中：" + team.shoot2SuccessCount + "/" + team.shoot2Count + " "
				+ (100.0 * team.shoot2SuccessCount / team.shoot2Count) + "%");
		System.out.println(team.name + "3分球命中：" + team.shoot3SuccessCount + "/" + team.shoot3Count + " "
				+ (100.0 * team.shoot3SuccessCount / (float) team.shoot3Count) + "%");
		System.out.println(team.name + "前场篮板：" + team.backboardA);
		System.out.println(team.name + "后场篮板：" + team.backboardD);
	}

	static class Game {
		private Team a;
		private Team d;

		public Game(Team a, Team b) {
			this.setA(a);
			this.setD(b);
		}

		public void swap() {
			Team t = getA();
			setA(getD());
			setD(t);
		}

		public ShootResult shoot() {
			return getA().shoot();
		}

		public Team getD() {
			return d;
		}

		public void setD(Team d) {
			this.d = d;
		}

		public Team getA() {
			return a;
		}

		public void setA(Team a) {
			this.a = a;
		}
	}

	/**
	 * 队伍
	 * 
	 * @author wanglongjiang
	 *
	 */
	static class Team {

		private String name;

		private int score;

		private int backboardA;

		private int backboardD;

		private boolean s2;

		/**
		 * 攻击次数
		 */
		private int attackCount = 0;

		/**
		 * 2分投篮命中率
		 */
		private float shooting2;

		private int shoot2Count = 0;
		private int shoot2SuccessCount = 0;

		/**
		 * 3分投篮命中率
		 */
		private float shooting3;
		private int shoot3Count = 0;
		private int shoot3SuccessCount = 0;

		Random random = new Random();

		/**
		 * 推进
		 * 
		 * @return
		 */
		public boolean advance() {
			return true;
		}

		/**
		 * 投篮
		 * 
		 * @return
		 */
		public ShootResult shoot() {
			attackCount++;
			float nextFloat = random.nextFloat();
			if (s2) {
				shoot2Count++;
				if (nextFloat < shooting2) {
					shoot2SuccessCount++;
					score += 2;
					return ShootResult.tow;
				}
			} else {
				shoot3Count++;
				if (nextFloat <= shooting3) {
					shoot3SuccessCount++;
					score += 3;
					return ShootResult.three;
				}
			}
			return ShootResult.zero;
		}

		public void incBackboardA() {
			backboardA++;
		}

		public void incBackboardD() {
			backboardD++;
		}
	}

	/**
	 * 裁判
	 * 
	 * @author wanglongjiang
	 *
	 */
	static class Referee {

	}

	static enum ShootResult {
		zero, tow, three
	}
}
