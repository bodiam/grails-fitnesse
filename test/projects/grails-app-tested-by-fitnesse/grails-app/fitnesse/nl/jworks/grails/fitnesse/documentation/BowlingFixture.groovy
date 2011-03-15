package nl.jworks.grails.fitnesse.documentation



public class BowlingFixture {
    public List doTable(List<List<String>> table) {
        Game game = new Game()
        List rollResults = ["", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""]
        List scoreResults = ["", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""]
        rollBalls(table, game)
        evaluateScores(game, table[1], scoreResults)
        return [rollResults, scoreResults]
    }

    private void evaluateScores(Game game, List<String> scoreRow, List scoreResults) {

        10.times { Integer frame ->
            int actualScore = game.score(frame + 1)
            int expectedScore = scoreRow[frameCoordinate(frame)] as int
            if (expectedScore == actualScore)
                scoreResults[frameCoordinate(frame)] = "pass"
            else
                scoreResults[frameCoordinate(frame)] = "Was:${actualScore}, expected:${expectedScore}."
        }
    }

    private int frameCoordinate(int frame) {
        return frame < 9 ? frame * 2 + 1 : frame * 2 + 2
    }

    private void rollBalls(List<List<String>> table, Game game) {
        List<String> rollRow = table[0]

        10.times { frame ->
            String firstRoll = rollRow[frame * 2]
            String secondRoll = rollRow[frame * 2 + 1]
            if (firstRoll.equalsIgnoreCase("X"))
                game.roll 10
            else {
                int firstRollInt = 0
                if (firstRoll == "-")
                    game.roll 0
                else {
                    firstRollInt = firstRoll as int
                    game.roll firstRollInt
                }
                if (secondRoll == "/") {
                    game.roll(10 - firstRollInt)
                } else if (secondRoll == ("-")) {
                    game.roll(0)
                } else {
                    game.roll secondRoll as int
                }
            }
        }
    }

    private class Game {
        int[] rolls = new int[21]
        int currentRoll = 0

        void roll(int pins) {
            rolls[currentRoll++] = pins
        }

        int score(int frame) {
            int score = 0
            int firstBall = 0

            frame.times {
                if (isStrike(firstBall)) {
                    score += 10 + nextTwoBallsForStrike(firstBall)
                    firstBall += 1
                } else if (isSpare(firstBall)) {
                    score += 10 + nextBallForSpare(firstBall)
                    firstBall += 2
                } else {
                    score += twoBallsInFrame(firstBall)
                    firstBall += 2
                }
            }
            return score
        }

        private int twoBallsInFrame(int firstBall) {
            return rolls[firstBall] + rolls[firstBall + 1]
        }

        private int nextBallForSpare(int firstBall) {
            return rolls[firstBall + 2]
        }

        private int nextTwoBallsForStrike(int firstBall) {
            return rolls[firstBall + 1] + rolls[firstBall + 2]
        }

        private boolean isSpare(int firstBall) {
            return rolls[firstBall] + rolls[firstBall + 1] == 10
        }

        private boolean isStrike(int firstBall) {
            return rolls[firstBall] == 10
        }
    }
}