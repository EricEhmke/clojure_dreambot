(ns dreambot-test.gui)

(import
 [javax.swing JFrame JPanel JLabel JCheckBox JButton JComboBox SwingUtilities]
 [java.awt Dimension BorderLayout GridLayout FlowLayout]
 [java.awt.event ActionListener]
 [org.dreambot.api Client]
 [org.dreambot.api.methods MethodProvider])

(defn createGui
  "Creates the GUI for script"
  [config]
  (let [frame (JFrame.)
        settingsPanel (new JPanel)
        fishAreaCombo (new JComboBox (into-array ["Catherby"]))
        fishTypeCombo (new JComboBox (into-array ["Lobster"]))
        cookCheckBox  (new JCheckBox "Cook Raw Fish")
        startButton (new JButton "Start Script")
        buttonPanel (JPanel.)]

    (.setTitle frame "Simple Fisher")
    (.setDefaultCloseOperation frame (JFrame/DISPOSE_ON_CLOSE))
    (.setLocationRelativeTo frame (.. Client getCanvas))
    (.setPreferredSize frame (new Dimension 300 170))
    (.. frame getContentPane (setLayout (new BorderLayout)))

    ;; Start of upper gui
    (.setLayout settingsPanel (new GridLayout 0 2))
    ;; Choose area to fish in
    ;; (.add settingsPanel (.setText (new JLabel) "Fishing Area"))
    (.add settingsPanel (new JLabel "Fishing Area"))
    (.add settingsPanel fishAreaCombo)
    ;; Choose type of fish
    (.add settingsPanel (new JLabel "Fish Type"))
    (.add settingsPanel fishTypeCombo)
    (.add settingsPanel cookCheckBox)
    (.. frame getContentPane (add settingsPanel BorderLayout/CENTER))

    (.setLayout buttonPanel (new FlowLayout))

    (.addActionListener startButton
                        (reify ActionListener
                          (actionPerformed [this e]
                            ;; mutate the items in the config
                            (MethodProvider/log "Button clicked")
                            (doto config
                              (. put ":cook" (.isSelected cookCheckBox))
                              (. put ":fishType" (str (.getSelectedItem fishTypeCombo)))
                              (. put ":fishingArea" (str (.getSelectedItem fishAreaCombo)))
                              (. put ":start" true))
                            (.dispose frame))))
    (.add buttonPanel startButton)

    (.. frame getContentPane (add buttonPanel BorderLayout/SOUTH))
    (.pack frame)
    (.setVisible frame true)
    ;; TODO: Add option to select cooking area
    ;; TODO: Add ability to power fish (drop all fish in place)
    ))

