(ns dreambot-test.core
(:import [org.dreambot.api.methods.MethodProvider :as MethodProvider])
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
  (MethodProvider/log "Hello from test script")
  1000)
