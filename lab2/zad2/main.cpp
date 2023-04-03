#include <iostream>

int gt_int(int &a, int &b)
{
    if (a > b)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

template <typename Iterator, typename Predicate>
Iterator mymax(
    Iterator first, Iterator last, Predicate pred)
{
    Iterator max = first;
    Iterator i;
    for (i = ++first; i != last; ++i)
    {
        if(pred(*i, *max) == 1){
            max = i;
        }
    }
    return max;
}

int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
int main()
{
    const int *maxint = mymax(&arr_int[0],
                              &arr_int[sizeof(arr_int) / sizeof(*arr_int)],
                              gt_int);
    std::cout << *maxint << "\n";
}