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
        fishAreaCombo (new JComboBox (into-array ["Catherby" "Draynor" "Lumbridge_Swamp" "Barbarian_Village" "Fishing_Guild"])) ;; Must match the key in the Areas map
        fishTypeCombo (new JComboBox (into-array ["Lobster" "Shrimp" "Anchovies" "Trout" "Salmon" "Pike" "Tuna" "Swordfish" "Shark" "Sardine" "Herring"]))
        ;;cookCheckBox  (new JCheckBox "Cook Raw Fish")
        startButton (new JButton "Start Script")
        buttonPanel (JPanel.)]

    (MethodProvider/log "In GUI")
    (.setTitle frame "Simple Fisher")

    (.setDefaultCloseOperation frame (JFrame/DISPOSE_ON_CLOSE))
    (.setLocationRelativeTo frame nil)
    (.setPreferredSize frame (new Dimension 300 170))
    (.. frame getContentPane (setLayout (new BorderLayout)))

    ;; Start of upper gui
    (.setLayout settingsPanel (new GridLayout 0 2))
    ;; Choose area to fish in
    (.add settingsPanel (new JLabel "Fishing Area"))
    (.add settingsPanel fishAreaCombo)
    ;; Choose type of fish
    (.add settingsPanel (new JLabel "Fish Type"))
    (.add settingsPanel fishTypeCombo)
    ;;(.add settingsPanel cookCheckBox)
    (.. frame getContentPane (add settingsPanel BorderLayout/CENTER))

    (.setLayout buttonPanel (new FlowLayout))

    (MethodProvider/log "At start button")
    (.addActionListener startButton
                        (reify ActionListener
                          (actionPerformed [this e]
                            ;; mutate the items in the config
                            (MethodProvider/log "GUI Closed/Scripted Started...")
                            (doto config
                              ;;(. put :cook (.isSelected cookCheckBox))
                              (. put :fishType (str (.getSelectedItem fishTypeCombo)))
                              (. put :fishingArea (str (.getSelectedItem fishAreaCombo)))
                              (. put :start true))
                            (.dispose frame))))
    (.add buttonPanel startButton)

    (.. frame getContentPane (add buttonPanel BorderLayout/SOUTH))
    (.pack frame)
    (.setVisible frame true)
    ;; TODO: Add option to select cooking area
    ;; TODO: Add ability to power fish (drop all fish in place)
    ))

