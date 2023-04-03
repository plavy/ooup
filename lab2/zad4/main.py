import math
import random


class DistributionTester:
    generator = None
    percentile_calculator = None

    def __init__(self, generator, percentile_calculator):
        self.generator = generator
        self.percentile_calculator = percentile_calculator

    def solve(self):
        array = self.generator.generate_array()
        print(f"Lista: {array}")
        for i in range(10, 100, 10):
            print(f"{i}. percentil {self.percentile_calculator.get_percentile(array, i)}")

class Generator:
    def generate_array(self):
        return -1

class SequentialGenerator(Generator):
    start = None
    end = None
    step = None

    def __init__(self, start, end, step):
        self.start = start
        self.end = end
        self.step = step
    
    def generate_array(self):
        return [i for i in range(self.start, self.end, self.step)]

class RandomGenerator(Generator):
    mean = None
    sigma = None
    size = None

    def __init__(self, mean, sigma, size):
        self.mean = mean
        self.sigma = sigma
        self.size = size

    def generate_array(self):
        return [random.normalvariate(self.mean, self.sigma) for i in range(self.size)]

def fibonacci(n):
    if n == 0:
        return 0
    elif n == 1 or n == 2:
        return 1
    else:
        return fibonacci(n-1) + fibonacci(n-2)

class FibonacciGenerator(Generator):
    size = None

    def __init__(self, size):
        self.size = size

    def generate_array(self):
        array = []
        for i in range(self.size):
            array.append(fibonacci(i))
        return array


class PercentileCalculator():
    def get_percentile(self, array, p):
        return -1

class NearestRank(PercentileCalculator):
    def get_percentile(self, array, p):
        return sorted(array)[math.ceil(p / 100 * len(array)) - 1]

class InterpolatedRank(PercentileCalculator):
    def get_percentile(self, array, p):
        array = sorted(array)
        if p < 100 * (1 - 0.5) / len(array):
            return array[0]
        if p > 100 * (len(array) - 0.5) / len(array):
            return array[-1]
        i = math.floor(p / 100 * len(array) + 0.5) - 1
        pi = 100 * (i+1 - 0.5) / len(array)
        return array[i] + len(array) * (p - pi) * (array[i + 1] - array[i]) / 100


def main():
    dt = DistributionTester(SequentialGenerator(1, 8, 2), NearestRank())
    dt.solve()

    dt = DistributionTester(RandomGenerator(1, 0.5, 10), InterpolatedRank())
    dt.solve()

    dt = DistributionTester(FibonacciGenerator(6), InterpolatedRank())
    dt.solve()


if __name__ == "__main__":
    main()