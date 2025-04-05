package ru.job4j.concurrent.pool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelSearchInArrayTest {

    @Test
    public void whenNeedFindInteger() {
        Integer[] intArray = {0, 9, 1, 8, 2, 7, 3, 6, 4, -1};
        int index = ParallelSearchInArray.search(intArray, 9);
        int expected = 1;
        assertThat(index).isEqualTo(expected);
    }

    @Test
    public void whenNeedFindDouble() {
        Double[] doubleArray = {0.15, 9.01, 1.2, 8.77, 2.01, 7.5, 3.9, 6.2, 4.0, -1.19, 10.11};
        int index = ParallelSearchInArray.search(doubleArray, 8.77);
        int expected = 3;
        assertThat(index).isEqualTo(expected);
    }

    @Test
    public void whenNeedFindString() {
        String[] namesOfTerrorists = {
                "Ушат Помоев", "Рулон Обоев", "Квартет Гобоев", "Улов Налимов", "Букет Левкоев", "Рекорд Надоев",
                "Отряд Ковбоев", "Подрыв Устоев", "Черёд Застоев", "Подшум Прибоев", "Погром Евреев", "Поджог Сараев",
                "Захват Покоев", "Исход Изгоев", "Подсуд Злодеев", "Обвал Забоев", "Угон Харлеев", "Загул Старлеев",
                "Удел Плебеев", "Камаз Отходов", "Развод Супругов", "Разгром Шалманов", "Друган Братанов",
                "Забег Дебилов", "Учёт Расходов", "Налог Сдоходов", "Парад Уродов", "Разбор Полётов", "Ремонт Трамваев",
                "Побег Злодеев", "Вагон Гондонов", "Отряд Кретинов", "Улов Кальмаров", "Запой Гусаров", "Сачок Моллюсков"
        };
        int index = ParallelSearchInArray.search(namesOfTerrorists, "Запой Гусаров");
        int expected = 33;
        assertThat(index).isEqualTo(expected);
    }

    @Test
    public void whenUseLinearSearch() {
        String[] rainbowColors = {"Каждый", "Охотник", "Желает", "Знать", "Где", "Сидит", "Фазан"};
        int arrayLength = rainbowColors.length;
        int index = ParallelSearchInArray.search(rainbowColors, "Знать");
        int expected = 3;
        assertThat(index).isEqualTo(expected);
        assertThat(arrayLength).isLessThan(ParallelSearchInArray.LINEAR_SEARCH_LIMIT);
    }

    @Test
    public void whenUseRecursiveSearch() {
        Integer[] ints = new Integer[100];
        int arrayLength = ints.length;
        for (int i = 0; i < arrayLength; i++) {
            ints[i] = i + 1;
        }
        int index = ParallelSearchInArray.search(ints, 77);
        int expected = 76;
        assertThat(index).isEqualTo(expected);
        assertThat(arrayLength).isGreaterThan(ParallelSearchInArray.LINEAR_SEARCH_LIMIT);
    }

    @Test
    public void whenNothingIsFound() {
        Integer[] ints = new Integer[100];
        int arrayLength = ints.length;
        for (int i = 0; i < arrayLength; i++) {
            ints[i] = i + 1;
        }
        int index = ParallelSearchInArray.search(ints, 101);
        int expected = -1;
        assertThat(index).isEqualTo(expected);
    }

    @Test
    public void whenNothingIsFoundInLinearSearch() {
        Integer[] ints = {100};
        int index = ParallelSearchInArray.search(ints, 101);
        assertThat(index).isNotEqualTo(0);
    }

    @Test
    public void whenUseEmptyArray() {
        Integer[] ints = {};
        int index = ParallelSearchInArray.search(ints, 101);
        assertThat(index).isEqualTo(-1);
    }

    @Test
    public void whenNeedToFindLastElement() {
        Integer[] intArray = {0, 9, 1, 8, 2, 7, 3, 6, 4, 99};
        int index = ParallelSearchInArray.search(intArray, 99);
        int expected = 9;
        assertThat(index).isEqualTo(expected);
        assertThat(index).isEqualTo(intArray.length - 1);
    }

    @Test
    public void whenNeedToFindLastElementInSmallArray() {
        Integer[] ints = {100};
        int index = ParallelSearchInArray.search(ints, 100);
        assertThat(index).isEqualTo(0);
        assertThat(index).isEqualTo(ints.length - 1);
    }
}