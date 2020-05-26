(ns c4-async.core
  (:import jline.console.ConsoleReader)
  (:use clojure.java.javadoc)
  (:require [clojure.core.async :as a]
            [clojure.string :as s]))


;; Helpers

(defn monitor [value]
  (println "Received value " value "of class" (class value))
  value)

;; Data

(def char->action
  {\a :left
   \d :right
   \s :down})

;; Core-Functions

;; -- Initializers

(defn init-board [width height]
  (apply vector
         (repeat width (apply vector (repeat height nil)))))

(defn init-state []
  {:current-player 0
   :cursor 0
   :players 2
   :board (init-board 4 4)})

(defn cursor-valid? [cursor board]
  (and ((complement neg?) cursor) (< cursor (count board))))

;; -- Move cursor

(defn move-cursor [diff {cursor :cursor board :board :as state}]
  (let [new-cursor (+ cursor diff)]
    (if (cursor-valid? new-cursor board)
      (assoc state :cursor new-cursor)
      state)))

(defn drop-column [token col]
  (if (some nil? col)
    (->> col
         (cons token)
         (drop-last)
         (into []))
    col))

;; -- Drop token

(defn toggle-player [players current-player]
  (-> current-player (inc) (mod players)))

(defn set-token [{:keys [current-player cursor players] :as state}]
  (-> state
    (update-in [:board cursor] (partial drop-column current-player))
    (update :current-player (partial toggle-player players))))

;; Printer

(def player-tokens "xo")

(defn token->char [token]
  (get player-tokens token \_))

(defn print-cursor [cursor width]
  (-> (repeat width " ")))

;; Action handlers

(def actions
 {:left (partial move-cursor -1)
  :right (partial move-cursor 1)
  :drop set-token})

;; Channels

(defn read-chars [out]
  (let [cr (ConsoleReader.)]
    (loop [ch (.readCharacter cr)]
      (a/>!! out (char ch))
      (recur (.readCharacter cr)))))

(defn user-input []
  (let [out (a/chan)]
    (a/thread (read-chars out))
    out))

(defn actions [in]
  (let [out (a/chan)
        xf (comp (keep char->action) (map monitor))]
    (a/pipeline 2 out xf in)
    out))

;; State

(defn -main
  "Run program"
  []
  (println "Starting game"))
