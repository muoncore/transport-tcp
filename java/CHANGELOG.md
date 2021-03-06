# Change Log

## [v0.0.9](https://github.com/muoncore/newton/tree/v0.0.9) (2017-05-15)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.8...v0.0.9)

**Implemented enhancements:**

- Remove @NewtonView for now in favour of an abstract method on BaseView [\#24](https://github.com/muoncore/newton/issues/24)

**Fixed bugs:**

- Extend eventstreamprocessor with regards to deserialisation of TenantEvent if unwrapped.  [\#26](https://github.com/muoncore/newton/issues/26)

**Merged pull requests:**

- Rework APIs, make use more consistent. Extend hierarchies with domain services.  [\#33](https://github.com/muoncore/newton/pull/33) ([daviddawson](https://github.com/daviddawson))

## [v0.0.8](https://github.com/muoncore/newton/tree/v0.0.8) (2017-05-15)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.7...v0.0.8)

**Implemented enhancements:**

- Remove spring auto-config scanning & replace with @EnableNewton [\#30](https://github.com/muoncore/newton/issues/30)
- Introduce abstract Base Domain Service [\#29](https://github.com/muoncore/newton/issues/29)

## [v0.0.7](https://github.com/muoncore/newton/tree/v0.0.7) (2017-05-10)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.6...v0.0.7)

## [v0.0.6](https://github.com/muoncore/newton/tree/v0.0.6) (2017-05-10)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.5...v0.0.6)

**Implemented enhancements:**

- Remove AggregateRootId type in favour of an annotation on the event class [\#25](https://github.com/muoncore/newton/issues/25)

**Closed issues:**

- Default context name to be the application name in aggregateroots [\#27](https://github.com/muoncore/newton/issues/27)
- Add in default codec for DocumentId [\#19](https://github.com/muoncore/newton/issues/19)

**Merged pull requests:**

- Rework IDs, extensions [\#28](https://github.com/muoncore/newton/pull/28) ([daviddawson](https://github.com/daviddawson))

## [v0.0.5](https://github.com/muoncore/newton/tree/v0.0.5) (2017-05-02)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.4...v0.0.5)

**Closed issues:**

- Should only create the repository class if it does not already exist [\#21](https://github.com/muoncore/newton/issues/21)
- Photon 'from' is being misused [\#20](https://github.com/muoncore/newton/issues/20)
- Provide a cold+hot subscription to an event aggregate [\#16](https://github.com/muoncore/newton/issues/16)
- Expose aggregate stream as a stream [\#15](https://github.com/muoncore/newton/issues/15)
- Need to sync up the stream names for UniqueAggregateDomainService [\#5](https://github.com/muoncore/newton/issues/5)

## [v0.0.4](https://github.com/muoncore/newton/tree/v0.0.4) (2017-04-24)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.3...v0.0.4)

**Closed issues:**

- Initiate saga processing direct from am event [\#12](https://github.com/muoncore/newton/issues/12)

## [v0.0.3](https://github.com/muoncore/newton/tree/v0.0.3) (2017-04-15)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.2...v0.0.3)

**Closed issues:**

- Reflections requires explicit jar file classpath loading in spring boot apps [\#14](https://github.com/muoncore/newton/issues/14)

## [v0.0.2](https://github.com/muoncore/newton/tree/v0.0.2) (2017-04-07)
[Full Changelog](https://github.com/muoncore/newton/compare/v0.0.1...v0.0.2)

**Closed issues:**

- Auto generate repositories [\#11](https://github.com/muoncore/newton/issues/11)
- Add view types: RebuildingDatastoreView, SharedDataStore, BaseView [\#10](https://github.com/muoncore/newton/issues/10)
- Automatically generate the repositories [\#4](https://github.com/muoncore/newton/issues/4)

## [v0.0.1](https://github.com/muoncore/newton/tree/v0.0.1) (2017-04-06)
**Merged pull requests:**

- Initial newton [\#9](https://github.com/muoncore/newton/pull/9) ([daviddawson](https://github.com/daviddawson))



\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*