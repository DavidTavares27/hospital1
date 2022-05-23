(ns hospital.aula4
  (:use [clojure.pprint])
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(defn chega-sem-malvado! [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa)
  (println "apos inserir" pessoa))

(defn simula-um-dia-em-parelelo
  "Simulação utilizando um mapv para forçar quase que imperativamente a execução lazy"
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]]
    (mapv #(.start (Thread. (fn [] (chega-sem-malvado! hospital %)))) pessoas)
    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))
  ;sem forçar situaçaio de retry (busy retry), pode ou nao acontecer
;(simula-um-dia-em-parelelo)


(defn simula-um-dia-em-parelelo-com-mapv-refatorada
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "6666"]
        starta-thread-de-chegada #(.start (Thread. (fn [] (chega-sem-malvado! hospital %))))]
    
    (mapv starta-thread-de-chegada pessoas)

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

;simula-um-dia-em-parelelo-com-mapv-refatorada)

;; (defn starta-thread-de-chegada
;;   ([hospital]
;;    (fn [pessoa] (starta-thread-de-chegada hospital pessoa)))
;;   ([hospital pessoa]
;;   (.start (Thread. (fn [] (chega-sem-malvado! hospital pessoa))))))

;; (defn simula-um-dia-em-parelelo-com-mapv-extraída
;;   []
;;   (let [hospital (atom (h.model/novo-hospital))
;;         pessoas ["111", "222", "333", "444", "555", "6666"]
;;        ;starta (starta-thread-de-chegada hospital)]

;;     (mapv starta pessoas)

;;     (.start (Thread. (fn [] (Thread/sleep 8000)
;;                        (pprint hospital))))))

;(simula-um-dia-em-parelelo-com-mapv-extraída)


(defn starta-thread-de-chegada
  [hospital pessoa]
   (.start (Thread. (fn [] (chega-sem-malvado! hospital pessoa)))))

(defn simula-um-dia-em-parelelo-com-partial
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "6666"]
        starta (partial starta-thread-de-chegada hospital)]

    (mapv starta pessoas)

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

;; (simula-um-dia-em-parelelo-com-partial)




(defn simula-um-dia-em-parelelo-com-doseq
  "Reamente estou preocupado em executar para os elementos da sequencia"
  []
  (let [hospital (atom (h.model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "6666"]]

    (doseq [pessoa pessoas]
      (starta-thread-de-chegada hospital pessoa))

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

;(simula-um-dia-em-parelelo-com-doseq)



(defn simula-um-dia-em-parelelo-com-dotimes
  "Reamente estou preocupado em executar N vezes"
  []
  (let [hospital (atom (h.model/novo-hospital))]

    (dotimes [pessoa 6]
      (starta-thread-de-chegada hospital pessoa))

    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

(simula-um-dia-em-parelelo-com-dotimes)




