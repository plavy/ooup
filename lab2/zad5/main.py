import sys
import time
import statistics
import datetime


class SlijedBrojeva:
    niz = None
    izvor = None
    akcije = []

    def __init__(self, izvor, akcije):
        self.niz = []
        self.izvor = izvor
        self.akcije = akcije

    def dodaj_broj(self, novi_broj):
        self.niz.append(novi_broj)
        for akcija in self.akcije:
            akcija.okini(self.niz)

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


class Akcija:
    def okini(self, niz):
        return -1

class SumaAkcija(Akcija):
    def okini(self, niz):
        print(f'Suma: {sum(niz)}')

class ProsjekAkcija(Akcija):
    def okini(self, niz):
        print(f'Prosjek: {sum(niz)/len(niz)}')

class MedijanAkcija(Akcija):
    def okini(self, niz):
        print(f'Medijan: {statistics.median(niz)}')

class DatotekaAkcija(Akcija):
    putanja = None

    def __init__(self, putanja='izlaz.txt'):
        self.putanja = putanja

    def okini(self, niz):
        with open(self.putanja, "a") as f:
            f.write(f"{datetime.datetime.now()} : {','.join(str(x) for x in niz)}\n")


def main():
    sb = SlijedBrojeva(TipkovnickiIzvor(), [ProsjekAkcija()])
    sb.kreni()

    print("---")

    sb = SlijedBrojeva(DatotecniIzvor("brojevi.txt"), [SumaAkcija(), DatotekaAkcija()])
    sb.kreni()

if __name__ == "__main__":
    main()
