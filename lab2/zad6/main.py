import re
import ast


def eval_expression(exp, variables={}):
  def _eval(node):
    if isinstance(node, ast.Num):
      return node.n
    elif isinstance(node, ast.Name):
      return variables[node.id]
    elif isinstance(node, ast.BinOp):
      return _eval(node.left) + _eval(node.right)
    else:
      raise Exception('Unsupported type {}'.format(node))

  node = ast.parse(exp, mode='eval')
  return _eval(node.body)

class Cell:
    parent = None
    ref = None
    exp = None
    value = None
    cells_to_notify = None

    def __init__(self, parent):
        self.parent = parent
        self.exp = '0'
        self.value = 0
        self.cells_to_notify = []

    def attach(self, cell):
        self.cells_to_notify.append(cell)

    def dettach(self, cell):
        self.cells_to_notify.remove(cell)

    def update(self):
        queue = [self]
        while len(queue) > 0:
            cell = queue.pop(0)
            if self in cell.cells_to_notify:
                # cell.exp = '0'
                raise RuntimeError(f"{cell.ref} already depends on {self.ref}")
            queue.extend(cell.cells_to_notify)
        self.parent.evaluate(self)
        self.notify()

    def notify(self):
        for cell in self.cells_to_notify:
            cell.update()

class Sheet:
    matrix = None

    def __init__(self, i, j):
        self.matrix = [[Cell(self) for y in range(j)] for x in range(i)]
    
    def get_pos_from_ref(self, ref):
        number = re.findall(r'[0-9]+', ref)[0]
        i = int(number) - 1
        letter = re.findall(r'[a-zA-Z]+', ref)[0]
        j = ord(letter.upper()) - 65 # only one letter is supported
        return i, j

    def cell(self, ref):
        i, j = self.get_pos_from_ref(ref)
        return self.matrix[i][j]

    def set(self, ref, exp):
        cell = self.cell(ref)
        cell.ref = ref
        dependencies = self.get_refs(cell)
        for dep_cell in dependencies:
            dep_cell.dettach(cell)
        cell.exp = exp
        dependencies = self.get_refs(cell)
        for dep_cell in dependencies:
            dep_cell.attach(cell)
        cell.update()

    def get_refs(self, cell):
        cells_refs = re.findall(r'[a-zA-Z]+[0-9]+', cell.exp)
        cells = []
        for ref in cells_refs:
            cells.append(self.cell(ref))
        return cells

    def evaluate(self, cell):
        refs = re.findall(r'[a-zA-Z]+[0-9]+', cell.exp)
        if len(refs) == 0:
            cell.value = eval_expression(cell.exp)
            return
        D = {}
        for ref in refs:
            cell_ref = self.cell(ref)
            self.evaluate(cell_ref)
            D[ref] = cell_ref.value
        cell.value = eval_expression(cell.exp, D)

    def print(self, values=True):
        for i in range(len(self.matrix)):
            for j in range(len(self.matrix[0])):
                cell = self.matrix[i][j]
                print(f"{cell.value if values else cell.exp}", end=" ")
            print("\n")

def main():
    s=Sheet(5,5)
    s.set('A1','2')
    s.set('A2','5')
    s.set('A3','A1+A2')
    s.set('A1','4')
    s.set('A4','A1+A3')
    s.set('A5','A4+2')
    s.print()
    print("---")

    try:
        # s.set('A1','A3')
        s.set('A1','A5')
    except RuntimeError as e:
        print("Caught exception:", e)
    s.print()


if __name__ == "__main__":
    main()