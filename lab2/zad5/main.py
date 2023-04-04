import sys
import time
import statistics
import datetime


class SlijedBrojeva:
    niz = None
    izvor = None
    akcija = None

    def __init__(self, izvor, akcija):
        self.niz = []
        self.izvor = izvor
        self.akcija = akcija

    def dodaj_broj(self, novi_broj):
        self.niz.append(novi_broj)
        self.akcija(self.niz)

    def kreni(self):
        last_time = time.time()
        while True:
            if time.time() - last_time > 1: # broj sekundi
                novi_broj = self.izvor.ucitaj_broj()
                if novi_broj == -1:
                    break
                self.dodaj_broj(novi_broj)
                last_time = time.time()

class Izvor:
    def ucitaj_broj(self):
        return -1

class TipkovnickiIzvor(Izvor):
    def ucitaj_broj(self):
        return int(sys.stdin.readline())

class DatotecniIzvor(Izvor):
    putanja = None
    f = None

    def __init__(self, putanja):
        self.putanja = putanja
        self.f = open(self.putanja, "r")

    def ucitaj_broj(self):
        broj = int(self.f.readline().strip())
        if broj == -1:
            self.f.close()
        return broj

def ispisi_sumu(niz):
    print(sum(niz))

def ispisi_prosjek(niz):
    print(sum(niz)/len(niz))

def ispisi_medijan(niz):
    print(statistics.median(niz))

def zapisi(niz):
    with open("izlaz.txt", "a") as f:
        f.write(f"{datetime.datetime.now()} : {','.join(str(x) for x in niz)}\n")

def main():
    sb = SlijedBrojeva(TipkovnickiIzvor(), ispisi_prosjek)
    sb.kreni()

    print("---")

    sb = SlijedBrojeva(DatotecniIzvor("brojevi.txt"), ispisi_sumu)
    sb.kreni()

if __name__ == "__main__":
    main()
