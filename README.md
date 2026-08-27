# Application d'analyse d'activités sportives

Application Android permettant de récupérer, analyser et visualiser vos activités sportives.

Le projet utilisait initialement **l'API Strava**. À la suite des changements concernant l'accès à cette API, les données sportives sont désormais récupérées via **Intervals.icu**, qui permet notamment de synchroniser automatiquement les activités provenant de Garmin.

## Fonctionnement

```text
Garmin Connect
      ↓
Intervals.icu
      ↓
Intervals.icu API
      ↓
Application Android
```

L'application exploite ensuite les données récupérées afin de proposer une visualisation détaillée des différentes activités sportives : course à pied, cyclisme, marche, etc.

## Resources

* Calendrier → https://github.com/kizitonwose/Calendar
* Graphiques → https://github.com/patrykandpatrick/vico
* API Intervals.icu → https://intervals.icu/api/v1/docs/
