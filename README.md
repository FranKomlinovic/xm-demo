# xm-demo
This project is demo for XM project

## Endpoints

### GET /normalized
Exposes an endpoint that will return a descending sorted list of all the cryptos,
comparing the normalized range (i.e. (max-min)/min)

**Response**

```
[
    {
        "symbol": "ETH",
        "normalizedRange": 0.6383810110763015
    },
    {
        "symbol": "XRP",
        "normalizedRange": 0.5060541310541311
    }
]
```

### GET /oldestNewestMinMax/{crypto}
Exposes an endpoint that will return the oldest/newest/min/max values for a requested
crypto

**Parameters**

|              Name | Required |  Type   | Description                                                                                                                                                                                   |
|------------------:|:--------:|:-------:|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|          `crypto` | required | string  | Code value of requested crypto                                                                                                               |

**Response**

```
{
    "oldest": {
        "timestamp": 1641009600000,
        "symbol": "BTC",
        "price": 46813.21
    },
    "newest": {
        "timestamp": 1643659200000,
        "symbol": "BTC",
        "price": 38415.79
    },
    "min": {
        "timestamp": 1643022000000,
        "symbol": "BTC",
        "price": 33276.59
    },
    "max": {
        "timestamp": 1641081600000,
        "symbol": "BTC",
        "price": 47722.66
    }
}
```


### GET /highestNormalized/{date}
Exposes an endpoint that will return the crypto with the highest normalized range for a
specific day

**Parameters**

|   Name | Required |       Type        | Description                                               |
|-------:|:--------:|:-----------------:|-----------------------------------------------------------|
| `date` | required | Date (yyyy-MM-dd) | Specific day we want to get highest normalized crypto for |

**Response**

```
{
    "symbol": "ETH",
    "normalizedRange": 0.024492327762341285
}
```
