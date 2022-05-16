(ns dreambot-test.core
  (:gen-class
   :name ^{org.dreambot.api.script.ScriptManifest
           {:name "Test Script"
            :description "A test script"
            :author "Eric"
            :version 1.0
            :category org.dreambot.api.script.Category/WOODCUTTING
            :image ""}} dreambot-test.core.TestScript
   :extends org.dreambot.api.script.AbstractScript))


(defn -onLoop [this]
  (org.dreambot.api.methods.MethodProvider/log "Hello from test script")
  1000)
