require: patterns/emoji.sc
require: lib/responses.js
require: lib/responseProcess.js


theme: /

    state: Start
        q!: $regex</start>
        go!: /Hello


    state: Hello
        timeout: /GoodBye || interval = "5m"
        a: Привет! 👋 Я бот, который понимает ваши эмоции через смайлики. Вот примеры смайликов, которые я понимаю:
        a: 😀 😊 😄 - если вам весело\n😔 🙁 😫 - если вам грустно\n😠 👿 😤 - если вы раздражены
        script:
            # Обнуление контекста сессии
            $context.session = {};

    # Когда пользователь отправляет радостное эмодзи
    state: Happy
        q!: $happyEmoji
        # Если пользователь не отвечает в течении 5 минут, то сессия завершается
        timeout: /GoodBye || interval = "5m"
        # Функция для получения нужного ответа
        a: {{ processEmotionResponse("happy", $session.prevEmotion, $request.query) }}
        script:
            # Сохранение отправленного эмодзи
            $session.prevEmotion = "happy"

    # Когда пользователь отправляет грустное эмодзи
    state: Sad
        q!: $sadEmoji
        timeout: /GoodBye || interval = "5m"
        a: {{ processEmotionResponse("sad", $session.prevEmotion, $request.query) }}
        script:
            $session.prevEmotion = "sad"

    # Когда пользователь отправляет злое эмодзи
    state: Angry
        q!: $angryEmoji
        timeout: /GoodBye || interval = "5m"
        a: {{ processEmotionResponse("angry", $session.prevEmotion, $request.query) }}
        script:
            $session.prevEmotion = "angry"

    # Для завершения сессии после длительного отсутствия
    state: GoodBye
        random:
            a: Кажется, наш разговор подошел к концу. Было приятно пообщаться! 👋
            a: Надеюсь, мы еще увидимся! Всего доброго! 👋
            a: Время нашей беседы истекло. До новых встреч! 👋
        script:
            $jsapi.stopSession();

    # Если пользователь отправляет другой эмодзи или эмодзи с текстом
    state: CatchAll
        event!: noMatch
        timeout: /GoodBye || interval = "5m"
        random:
            a: Извините, но я понял вашего сообщения. Попробуйте использовать один из этих эмодзи: 😀 😔 😠
            a: Мне трудно вас понять. Давайте использовать эмодзи радости (😀), грусти (😔) или злости (😠)
        script:
            # Обнуление контекста сессии
            $context.session = {};

    state: Match
        event!: match
        a: {{$context.intent.answer}}
