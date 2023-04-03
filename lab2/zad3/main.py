def mymax(iterable, key=lambda x: x):
    max_x=None
    max_key=None

    for x in iterable:
        if not max_key or key(x) > max_key:
            max_x = x
            max_key = key(x)

    return max_x


def main():
    stringlist = [
        "Gle", "malu", "vocku", "poslije", "kise",
        "Puna", "je", "kapi", "pa", "ih", "njise"]
    maxlen = mymax(stringlist, lambda x: len(x))
    print(maxlen)
    maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
    print(maxint)
    maxchar = mymax("Suncana strana ulice")
    print(maxchar)
    maxstring = mymax(stringlist)
    print(maxstring)
    D={'burek':8, 'buhtla':5}
    maxdict = mymax(D, D.get) # iterira se po ključevima, na svakom ključu se poziva metoda get
    print(maxdict)
    lastperson = mymax([("Ana", "Zub"), ("Buga", "Zub"), ("Ivan", "Mesić")], lambda x: (x[1], x[0]))
    print(lastperson)


if __name__ == "__main__":
    main()
