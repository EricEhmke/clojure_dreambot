(ns dreambot-test.gui)

(import
 [javax.swing JFrame]
 [javax.swing JPanel]
 [javax.swing JLabel]
 [javax.swing.JComponent JComboBox]
 [java.awt Dimension]
 [java.awt BorderLayout]
 [java.awt GridLayout]

 [org.dreambot.api Client])

(defn createGui
  "Creates the GUI for script"
  []
  (let [frame (JFrame.)
        settingsPanel (JPanel.)]
    (.setTitle frame "Simple Fisher")
    (.setDefaultCloseoperation frame (JFrame/DISPOSE_ON_CLOSE))
    (.setLocationRelativeTo frame (.. Client getClient getInstance getCanvas))
    (.setPreferredSize frame (new Dimension 300 170))
    (.. frame (getContentPane) (setLayout (new BorderLayout)))

    ;; Start of upper gui
    (.setLayout settingsPanel (new GridLayout 0 2))

    (.add settingsPanel (.setText (JLabel.) "Fishing Area"))
    (.add settingsPanel (new JComboBox (into-array ["Catherby"])))

    (.add settingsPanel (.setText (JLabel.) "Fish Type"))
    (.add settingsPanel (new JComboBox (into-array ["Lobster"])))))
