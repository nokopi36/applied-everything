---
navigation:
  parent: applied_everything/index.md
  title: 特異点圧縮機
  icon: singularity_compressor
  position: 10
item_ids:
- singularity_compressor
---

# 特異点圧縮機

<BlockImage id="singularity_compressor" scale="6" />

<ItemLink id="ae2:singularity" /> 9 個を <ItemLink id="compressed_singularity" /> 1 個にする機械です。
圧縮特異点は、この機械でしか作れません。

## 電力

* 1 回の圧縮で **10,000,000 AE**（FE なら 20,000,000 FE）を消費します。
* 加速カードなしでは **5,000 AE/t** ずつ吸い込むので、1 回に 100 秒かかります。
* つながっている ME ネットワークから電力を取ります。FE のケーブルを直接つないでも動きます。
* 内部に溜められる電力は 2 ティック分だけなので、前もって貯めておくことはできません。電力が足りないときは
  その分だけ遅くなり、進み具合は失われません。
* 電力を吸い込むのは、特異点が 9 個以上あり、出力に空きがあるときだけです。
* 特異点を取り出して 9 個未満にすると、進み具合はリセットされます。それまでに使った電力は戻りません。

## 加速カード

<ItemLink id="ae2:speed_card" /> を 4 枚まで入れられます。1 枚ごとに吸い込む速さが 5,000 AE/t 増え、4 枚なら
1 回 20 秒です。**1 回に使う電力の合計は変わりません**。速くなるだけです。

## 自動化

* 特異点はどの面からでも入れられます（ホッパー、<ItemLink id="ae2:interface" />、
  <ItemLink id="ae2:pattern_provider" /> など）。
* 圧縮特異点はどの面からでも取り出せます。入力の特異点は取り出せません。
* 圧縮機は、できたものを自分で押し出しません。自動クラフトでは、圧縮機の隣のパターンプロバイダーに
  加工パターン（特異点 9 個 → 圧縮特異点 1 個）を入れ、完成品は <ItemLink id="ae2:import_bus" />
  かインターフェースでネットワークに戻してください。

## レシピ

<RecipeFor id="singularity_compressor" />
