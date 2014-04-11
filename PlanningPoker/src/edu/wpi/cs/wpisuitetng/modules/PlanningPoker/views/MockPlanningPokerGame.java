
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author atrose
 */
public class MockPlanningPokerGame {

    public MockPlanningPokerGame() {

    }

    public String getGameName() {
        return "Austin's planning poker game.";
    }

    public ArrayList<MockRequirement> getRequirements() {
        ArrayList<MockRequirement> toReturn = new ArrayList<MockRequirement>();

        MockRequirement r1 = new MockRequirement("Name 1", "Description 1");
        MockRequirement r2 = new MockRequirement("Name 2", "Description 2");

        toReturn.add(r1);
        toReturn.add(r2);

        return toReturn;
    }

    public boolean hasDeadline() {
        return true;
    }

    public String getDeadlinestring() {
        return "May 01 at 12:52";
    }

    public ArrayList<Integer> getSelectedCardIndices(MockUser user, MockRequirement requirement) {
        
        if (requirement.getName().equals("Name 1")) {
            return new ArrayList<Integer>() {
                {
                    add(0);
                    add(2);
                    add(3);
                }
            };
        }
        
        if (requirement.getName().equals("Name 2")) {
            return new ArrayList<Integer>() {
                {
                    add(1);
                    add(5);
                }
            };
        }
        
        return new ArrayList<Integer>();

    }

    public ArrayList<Integer> getDeckValues() {
        return new ArrayList<Integer>() {
            {
                add(1);
                add(1);
                add(2);
                add(3);
                add(5);
                add(8);
            }
        };
    }

}
