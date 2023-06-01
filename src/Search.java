import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Search {
    private GamePanel gamePanel;
    boolean currentlySearching;
    private Timer animationTimer;
    private int dotsCount = 0;
    private String searchingText = "Searching";

    public Search(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        currentlySearching = false;
    }

    public void performSearch() {
        if (gamePanel.isInBattle()) {
            JOptionPane.showMessageDialog(gamePanel, "You are already in battle!");
            return;
        }

        SwingWorker<Void, Void> searchWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                gamePanel.enableSearchButton(false);
                currentlySearching = true;

                gamePanel.setSearchLabelText("SEARCHING...");
                gamePanel.showSearchingLabel(true);

                animationTimer = new Timer(500, e -> {
                    animateDots();
                    gamePanel.setSearchLabelText(searchingText);
                });
                animationTimer.setInitialDelay(0);
                animationTimer.start();

                JLabel loadingLabel = new JLabel(new ImageIcon("images/searching.gif"));
                loadingLabel.setBounds(70, 10, 100, 100);
                gamePanel.add(loadingLabel);
                gamePanel.revalidate();
                gamePanel.repaint();

                int searchTime = (int) (Math.random() * 7000) + 1000;

                Timer timer = new Timer(searchTime, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        gamePanel.remove(loadingLabel);
                        gamePanel.revalidate();
                        gamePanel.repaint();
                        gamePanel.showSearchingLabel(false);
                        animationTimer.stop();

                        String enemyPokeKalye = getRandomEnemyPokeKalye();
                        gamePanel.setEnemyImage(enemyPokeKalye);

                        String dialogue = " " + enemyPokeKalye + " has appeared!\n What will "
                                + gamePanel.getPokeKalyeName()
                                + " do?";
                        gamePanel.setDialogueText(dialogue);

                        currentlySearching = false;
                        gamePanel.setInBattle(true);
                    }
                });
                timer.setRepeats(false);
                timer.start();

                return null;
            }
        };

        searchWorker.execute();
    }

    public boolean isInBattle() {
        return gamePanel.isInBattle() || currentlySearching;
    }

    private String getRandomEnemyPokeKalye() {
        int level = gamePanel.getLevel();
        if (level < 10) {
            String[] veryCommonEnemies = { "Ipis", "Daga" };
            String[] commonEnemies = { "Lamok", "Langaw", "Tuta", "Ibon" };
            String[] moderateEnemies = { "Kuting", "Manok", "Gagamba", "Butiki", "Kuto", "Paro-paro" };
            String[] rareEnemies = { "Salagubang", "Langgam", "Tambay", "Palaka", "Ahas", "Higad", "Tipaklong" };

            double random = Math.random();
            if (random < 0.1) {
                return getRandomArrayElement(rareEnemies);
            } else if (random < 0.4) {
                return getRandomArrayElement(moderateEnemies);
            } else if (random < 0.7) {
                return getRandomArrayElement(commonEnemies);
            } else {
                return getRandomArrayElement(veryCommonEnemies);
            }
        } else if (level >= 10 && level <= 19) {
            String[] veryCommonEnemies = { "Askal", "Palaka" };
            String[] commonEnemies = { "Flying ipis", "Dagang Kanal", "Bangaw",
                    "Puspin", "Tutubi", "Paro-paro" };
            String[] moderateEnemies = { "Paniki", "Antik", "Higad", "Salagubang", "Manok", "Ahas", "Tipaklong",
                    "Colored Sisiw", "Tuko" };
            String[] rareEnemies = { "Kambing", "Jejemonster", "Kabayo" };

            double random = Math.random();
            if (random < 0.2) {
                return getRandomArrayElement(rareEnemies);
            } else if (random < 0.5) {
                return getRandomArrayElement(moderateEnemies);
            } else if (random < 0.8) {
                return getRandomArrayElement(commonEnemies);
            } else {
                return getRandomArrayElement(veryCommonEnemies);
            }
        }
        return "Splinter";
    }

    private String getRandomArrayElement(String[] array) {
        int index = (int) (Math.random() * array.length);
        return array[index];
    }

    private void animateDots() {
        dotsCount++;
        if (dotsCount > 4) {
            dotsCount = 0;
        }

        StringBuilder dots = new StringBuilder();
        for (int i = 0; i < dotsCount; i++) {
            dots.append(".");
        }
        searchingText = "SEARCHING" + dots;
    }
}
