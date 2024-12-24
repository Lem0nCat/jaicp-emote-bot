// Состояния настроений
var emotions = {
    HAPPY: 'happy',
    SAD: 'sad',
    ANGRY: 'angry'
};


// Функция для получения случайного элемента в массиве
function getRandomElement(array) {
    return array[Math.floor(Math.random() * array.length)];
}

// Функция для выбора случайного текстового ответа по эмоциям
function selectEmotionResponse(currentEmotion, prevEmotion) {
    var emotionResponses = getResponsesForEmotion(currentEmotion, prevEmotion);
    if (!emotionResponses) {
        return "Я не совсем понимаю твои чувства";
    }
    return getRandomElement(emotionResponses);
}

// Функция возвращает эмодзи(другой) в зависимости от настроения пользователя
function selectRandomEmoji(emotion, userEmoji) {
    var emojis = {
        "happy": ["😀", "😃", "😄", "😁", "😆", "😂", "😊", "🤩", "🥳"],
        "sad": ["😒", "😞", "😔", "😟", "😕", "🙁", "😫", "😩", "😓"],
        "angry": ["👿", "😬", "😤", "😡", "👺", "💀", "😠", "🤯", "🤨"]
    };

    // Проверка валидности входных параметров
    if (!emotion || !emojis[emotion]) {
        return emojis.happy[0]; // Возвращаем дефолтный эмодзи
    }

    // Получаем массив эмодзи для нужной эмоции
    var availableEmojis = emojis[emotion];
    
    // Находим индекс эмодзи пользователя
    var index = availableEmojis.indexOf(userEmoji);
    
    // Если эмодзи найден, удаляем его
    if (index > -1) {
        availableEmojis.splice(index, 1);
    }

    // Возвращаем случайный эмодзи или первый из группы, если массив пуст
    return availableEmojis.length > 0 
        ? getRandomElement(availableEmojis)
        : emojis[emotion][0];
}

// Функция возвращает ответ пользователю в зависимости от его настроения и эмодзи
function processEmotionResponse(emotion, prevEmotion, userEmoji) {
    // Проверка входных параметров
    if (!emotion || !userEmoji) {
        return 'Извините, произошла ошибка 😔';
    }
    
    var response = selectEmotionResponse(emotion, prevEmotion);

    // Вызываем функцию selectRandomEmoji и прибавляем результат к строке ответа
    var emoji = selectRandomEmoji(emotion, userEmoji);
    return response + ' ' + emoji;
}
